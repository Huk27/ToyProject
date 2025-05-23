package com.Jinhyy.service.search;

import com.Jinhyy.KaKaoSearchRespoitory;
import com.Jinhyy.NaverSearchRespoitory;
import com.Jinhyy.entity.SearchCount;
import com.Jinhyy.constants.Constants;
import com.Jinhyy.external.search.request.KaKaoExternalSearchRequest;
import com.Jinhyy.external.search.request.NaverExternalSearchRequest;
import com.Jinhyy.external.search.response.KaKaoExternalSearchResponse;
import com.Jinhyy.external.search.response.NaverExternalSearchResponse;
import com.Jinhyy.repository.h2.SearchCountRepository;
import com.Jinhyy.search.BlogInfo;
import com.Jinhyy.search.response.SearchHot10Response;
import com.Jinhyy.search.response.SearchResponse;
import com.Jinhyy.search.type.SearchDomainType;
import com.Jinhyy.search.type.SortType;
import com.Jinhyy.service.search.converter.ExternalSearchResponseConverter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.lang.Math.round;

@Slf4j
@Service
@EnableScheduling
public class SearchService {

    private final KaKaoSearchRespoitory kaKaoSearchRespoitory;
    private final NaverSearchRespoitory naverSearchRespoitory;
    private final ExternalSearchResponseConverter externalSearchResponseConverter;
    private final SearchCountRepository searchCountRepository;

    private final AtomicBoolean syncRunning = new AtomicBoolean(false);

    LoadingCache<String, AtomicLong> searchIncrementCountStoreCache = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build(this::buildViewCountIncrementStoreCache)
            ;

    LoadingCache<String, AtomicLong> searchTotalCountStoreCache = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build(this::findFromDB)
            ;

    LoadingCache<String, List<SearchHot10Response.SearchCountInfo>> hotSearchCountStoreCache = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build(this::buildHotTop10SearchKeyword)
            ;

    public SearchService(KaKaoSearchRespoitory kaKaoSearchRespoitory, NaverSearchRespoitory naverSearchRespoitory,
                         ExternalSearchResponseConverter externalSearchResponseConverter, SearchCountRepository searchCountRepository) {
        this.kaKaoSearchRespoitory = kaKaoSearchRespoitory;
        this.naverSearchRespoitory = naverSearchRespoitory;
        this.externalSearchResponseConverter = externalSearchResponseConverter;
        this.searchCountRepository = searchCountRepository;
    }

    @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
    public void syncIncrementDataToDBAndExpireHot() {
        if (syncRunning.compareAndSet(false, true)) {
            log.warn("## sync start..");
            try {
                ConcurrentMap<String, AtomicLong> copiedConcurrentMap = new ConcurrentHashMap<>();
                copiedConcurrentMap.putAll(searchIncrementCountStoreCache.asMap());
                searchIncrementCountStoreCache.invalidateAll();
                hotSearchCountStoreCache.invalidateAll();
                Flux.fromIterable(copiedConcurrentMap.entrySet())
                        .flatMap(entry -> Mono.fromCallable(() -> {
                            log.warn("## increase sync[{}] : +{}", entry.getKey(), entry.getValue().get());
                            searchCountRepository.increaseCount(entry.getKey(), entry.getValue().get());
                            return entry;
                        }))
                        .subscribe();
            } finally {
                syncRunning.set(false);
            }
        }
    }

    private List<SearchHot10Response.SearchCountInfo> buildHotTop10SearchKeyword(String key) {
        return searchCountRepository.FindTop10OrderByCountDesc()
                .stream().map(r -> externalSearchResponseConverter.searchCountEntityToSearchCountInfo(r)).toList();
    }
    private AtomicLong buildViewCountIncrementStoreCache(String keyWord) {
        return new AtomicLong(0L);
    }

    private AtomicLong findFromDB(String keyWord) {
        SearchCount searchCount = searchCountRepository.findByKeyword(keyWord);
        if (searchCount == null) {
            searchCount = searchCountRepository.save(new SearchCount().setKeyword(keyWord).setCount(0L));
        }
        return new AtomicLong(searchCount.getCount());
    }

    public Mono<SearchResponse> search(String query, SortType sortType, Integer page, Integer pageSize) {

        Mono<Long> saveIncrementIntoCache = Mono.fromCallable(() -> searchTotalCountStoreCache.get(query))
                .switchIfEmpty( Mono.fromCallable(() -> {
                    SearchCount entity = searchCountRepository.save(new SearchCount().setKeyword(query).setCount(0l));
                    searchTotalCountStoreCache.put(entity.getKeyword(), new AtomicLong(entity.getCount()));
                    return entity;
                }).map(r -> new AtomicLong(r.getCount())))
                .flatMap(count -> {
                    Long updated = searchIncrementCountStoreCache.get(query).addAndGet(1l);
                    searchTotalCountStoreCache.get(query).addAndGet(1l);
                    log.warn("## keyword[{}] updated ==> {}", query, updated);
                    return Mono.just(count.get());
                })
                .defaultIfEmpty(0L)
                ; 

        Mono<SearchResponse> kakaoCall = Mono.zip(
                    saveIncrementIntoCache,
                    kaKaoSearchRespoitory.search(new KaKaoExternalSearchRequest().setQuery(query).setSortType(sortType).setPage(page).setSize(pageSize))
                )
                .flatMap(zipped -> {
                    KaKaoExternalSearchResponse response = zipped.getT2();
                    KaKaoExternalSearchResponse.SearchMeta searchMeta = response.getSearchMeta();
                    final int adjustedTotalCount = Math.min(Constants.MAXIMUM.KAKAO_TOTAL_COUNT, searchMeta.getPageableCount());
                    final int totalPage = Math.round(adjustedTotalCount / pageSize) + 1;
                    final List<BlogInfo> blogInfos = response.getDocuments().stream().map(externalSearchResponseConverter::kakaoToBlogInfo).collect(Collectors.toList());

                    return Mono.just(
                            new SearchResponse()
                                    .setTotalCount(adjustedTotalCount).setSearchDomainType(SearchDomainType.KAKAO)
                                    .setTotalPage(totalPage)
                                    .setRequestedPage(page)
                                    .setBlogInfos(blogInfos)
                                    .setSortType(sortType)
                                    .setEol(page >= totalPage)
                    );
                })
                .defaultIfEmpty(new SearchResponse())
                ;

        Mono<SearchResponse> naverCall = Mono.zip(
                    saveIncrementIntoCache,
                    naverSearchRespoitory.search(new NaverExternalSearchRequest().setQuery(query).setSortType(sortType).setStart(page, pageSize).setDisplay(pageSize))
                )
                .map(zipped -> {
                    NaverExternalSearchResponse r = zipped.getT2();
                    final int adjustedTotalCount = Math.min(Constants.MAXIMUM.NAVER_TOTAL_COUNT, r.getTotalCount());
                    final int totalPage = round(adjustedTotalCount / pageSize) + 1;
                    final List<BlogInfo> blogInfos = r.getItems().stream().map(externalSearchResponseConverter::naverToBlogInfo).collect(Collectors.toList());

                    return new SearchResponse()
                            .setTotalCount(adjustedTotalCount).setSearchDomainType(SearchDomainType.NAVER)
                            .setTotalPage(totalPage)
                            .setRequestedPage(page)
                            .setBlogInfos(blogInfos)
                            .setSortType(sortType)
                            .setEol(page >= totalPage)
                            ;
                });

        return kakaoCall.onErrorResume(throwable -> naverCall);
    }

    public Mono<SearchHot10Response> serachHot10() {
        // language or key
        return Mono.fromCallable(() -> hotSearchCountStoreCache.get(Constants.HOT10.key))
                .map(r -> new SearchHot10Response().setSearchCountInfos(r));
    }
}
