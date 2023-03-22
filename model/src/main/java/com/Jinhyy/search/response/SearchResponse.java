package com.Jinhyy.search.response;

import com.Jinhyy.search.BlogInfo;
import com.Jinhyy.search.type.SearchDomainType;
import com.Jinhyy.search.type.SortType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@ToString
@Getter
@Setter
@Accessors(chain = true)
public class SearchResponse {

    private SortType sortType;
    private SearchDomainType searchDomainType;
    private Integer totalCount; // 카카오는 2500개, 네이버는 1100개 제한임.

    private Integer totalPage; // 반올림으로. (totalCount / size)
    private Integer requestedPage; // 카카오=> page, 네이버=>  (start / display) + 1
    private List<BlogInfo> blogInfos;

    private Boolean eol;
}
