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
public class KaKaoExternalSearchResponse {
    @JsonProperty("documents")
    private List<Document> documents;

    @JsonProperty("meta")
    private SearchMeta searchMeta;

    @Setter
    @Getter
    @ToString
    @Accessors(chain = true)
    public static class Document {
        private String title;
        private String contents;
        private String url;
        @JsonProperty("blogname")
        private String blogName;
        private String thumbnail;
        private String datetimeStr;
    }

    @Setter
    @Getter
    @ToString
    @Accessors(chain = true)
    public static class SearchMeta {
        @JsonProperty("total_count")
        private Integer totalCount;
        @JsonProperty("pageable_count")
        private Integer pageableCount;
        @JsonProperty("is_end")
        private Boolean isEnd;
    }
}
