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
public class SearchHot10Response {

    List<SearchCountInfo> searchCountInfos;

    @ToString
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class SearchCountInfo {
        private String keyword;
        private Long count;
    }
}
