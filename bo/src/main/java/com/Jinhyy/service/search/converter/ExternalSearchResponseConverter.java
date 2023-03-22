package com.Jinhyy.service.search.converter;

import com.Jinhyy.entity.SearchCount;
import com.Jinhyy.external.search.response.KaKaoExternalSearchResponse;
import com.Jinhyy.external.search.response.NaverExternalSearchResponse;
import com.Jinhyy.search.BlogInfo;
import com.Jinhyy.search.response.SearchHot10Response;
import com.Jinhyy.search.type.SearchDomainType;
import org.springframework.stereotype.Service;

@Service
public class ExternalSearchResponseConverter {

    public BlogInfo kakaoToBlogInfo(KaKaoExternalSearchResponse.Document document) {
        return new BlogInfo()
                .setBlogName(document.getBlogName())
                .setContents(document.getContents())
                .setTitle(document.getTitle())
                .setUrl(document.getUrl())
                .setDomainType(SearchDomainType.KAKAO)
                .setThumbnail(document.getThumbnail());
    }

    public BlogInfo naverToBlogInfo(NaverExternalSearchResponse.Item item) {
        return new BlogInfo()
                .setBlogName(item.getBloogerName())
                .setContents(item.getDescription())
                .setTitle(item.getTitle())
                .setUrl(item.getLink())
                .setDomainType(SearchDomainType.NAVER)
        ;
    }

    public SearchHot10Response.SearchCountInfo searchCountEntityToSearchCountInfo(SearchCount searchCount) {
        return new SearchHot10Response.SearchCountInfo().setKeyword(searchCount.getKeyword()).setCount(searchCount.getCount());
    }
}
