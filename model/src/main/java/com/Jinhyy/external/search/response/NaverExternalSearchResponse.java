package com.Jinhyy.external.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class NaverExternalSearchResponse extends NaverBaseResponse{
    @JsonProperty("total")
    private Integer totalCount;
    @JsonProperty("start")
    private Integer start;
    @JsonProperty("display")
    private Integer display;
    @JsonProperty("items")
    private List<Item> items;

    @Setter
    @Getter
    @ToString
    @Accessors(chain = true)
    public static class Item {
        private String title;
        private String description;
        private String link;
        @JsonProperty("bloggername")
        private String bloogerName;
        @JsonProperty("bloggerlink")
        private String bloggerLink;
        @JsonProperty("postdate")
        private String postDate;
    }
}
