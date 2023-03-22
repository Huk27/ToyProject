package com.Jinhyy;

import com.Jinhyy.external.search.request.NaverExternalSearchRequest;
import com.Jinhyy.external.search.response.KaKaoExternalSearchResponse;
import com.Jinhyy.external.search.response.NaverExternalSearchResponse;
import com.Jinhyy.search.type.SearchDomainType;
import com.Jinhyy.search.type.SortType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Consumer;

@Repository
@Slf4j
public class NaverSearchRespoitory {
    @Value("${app.naver.path}")
    private String searchPath;

    Consumer<HttpHeaders> NAVER_DEFAULT_HEADER_CONSUMER = (httpHeaders) -> {
        httpHeaders.add("X-Naver-Client-Id", "4M2hbg8iOsdtIxx6IaS4");
        httpHeaders.add("X-Naver-Client-Secret", "kuHcgkunJ9");
    };
    private final WebClient naverSearchWebClient;

    public NaverSearchRespoitory(@Qualifier("naverSearchWebClient") WebClient naverSearchWebClient) {
        this.naverSearchWebClient = naverSearchWebClient;
    }

    public Mono<NaverExternalSearchResponse> search(NaverExternalSearchRequest request) {
        request.verifyAndAdjust();
        return naverSearchWebClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder.path(searchPath)
                            .queryParam("query", request.getQuery())
                            .queryParam("sort", SortType.convertToStringBySearchDomainType(SearchDomainType.NAVER, request.getSortType()))
                            .queryParam("start", request.getStart())
                            .queryParam("display", request.getDisplay())
                            .build()
                )
                .headers(NAVER_DEFAULT_HEADER_CONSUMER)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(NaverExternalSearchResponse.class);
                    }
                    return clientResponse.releaseBody()
                            .doFirst(() -> log.error("## naver search fail [{}]. ==> {}", clientResponse.statusCode(), request))
                            .thenReturn(new NaverExternalSearchResponse()
                                    .setTotalCount(0)
                                    .setItems(new ArrayList<>())
                            );
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                ;
    }
}
