package com.Jinhyy;

import com.Jinhyy.external.search.request.KaKaoExternalSearchRequest;
import com.Jinhyy.external.search.response.KaKaoExternalSearchResponse;
import com.Jinhyy.search.type.SearchDomainType;
import com.Jinhyy.search.type.SortType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;

@Repository
@Slf4j
public class KaKaoSearchRespoitory {

    @Value("${app.kakao.path}")
    private String searchPath;
    private final WebClient searchWebClient;

    public KaKaoSearchRespoitory(@Qualifier("kakaoSearchWebClient") WebClient searchWebClient) {
        this.searchWebClient = searchWebClient;
    }
    public Mono<KaKaoExternalSearchResponse> search(KaKaoExternalSearchRequest request) {
        request.verifyAndAdjust();
        return searchWebClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder.path(searchPath)
                            .queryParam("query", request.getQuery())
                            .queryParam("sort", SortType.convertToStringBySearchDomainType(SearchDomainType.KAKAO, request.getSortType()))
                            .queryParam("page", request.getPage())
                            .queryParam("size", request.getSize())
                            .build()
                )
                .header("Authorization", "KakaoAK 38b2082713ac3eefbc25f3a1681c1ba1")
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(KaKaoExternalSearchResponse.class);
                    }
                    return clientResponse.releaseBody()
                            .doFirst(() -> log.error("## kakao search fail [{}]. ==> {}", clientResponse.statusCode(), request))
                            .thenReturn(new KaKaoExternalSearchResponse()
                                    .setSearchMeta(new KaKaoExternalSearchResponse.SearchMeta().setPageableCount(0))
                                    .setDocuments(new ArrayList<>())
                            );
                })
                .retryWhen(Retry.fixedDelay(5, Duration.ofMillis(100)))
                ;
    }
}
