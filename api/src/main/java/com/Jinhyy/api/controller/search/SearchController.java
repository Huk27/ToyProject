package com.Jinhyy.api.controller.search;

import com.Jinhyy.search.request.SearchRequest;
import com.Jinhyy.search.response.SearchHot10Response;
import com.Jinhyy.search.response.SearchResponse;
import com.Jinhyy.service.search.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/search")
@Validated
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/blog")
    public Mono<SearchResponse> searchBlog(@RequestBody Mono<SearchRequest> request) {
        return request.flatMap(r -> searchService.search(r.getQuery(), r.getSortType(), r.getPage(), r.getPageSize()));
    }

    @GetMapping("/hot")
    public Mono<SearchHot10Response> getHotSearch() {
        return searchService.serachHot10();
    }


}
