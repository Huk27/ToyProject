package com.Jinhyy.search;

import com.Jinhyy.search.type.SearchDomainType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class BlogInfo {
    SearchDomainType domainType;
    private String blogName;
    private String thumbnail;   // kakao only.
    private String url;
    private String title;
    private String contents;
    private LocalDateTime datetime;
}
