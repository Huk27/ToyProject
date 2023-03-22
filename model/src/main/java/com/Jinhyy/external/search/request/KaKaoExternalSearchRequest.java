package com.Jinhyy.external.search.request;

import com.Jinhyy.error.CustomRuntimeException;
import com.Jinhyy.search.type.SortType;
import com.Jinhyy.util.Is;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KaKaoExternalSearchRequest {

    protected String query;
    protected SortType sortType;
    protected Integer page;
    protected Integer size;

    public void verifyAndAdjust() {
        if (Is.Negative(size) || Is.Negative(page) || (query != null && query.isBlank())) {
            throw CustomRuntimeException.EXCEPTION_REQUEST_ARGUMENT_INVALID();
        }

        if (page > 50) {
            page = 50;
        }

        if (size > 50) {
            size = 50;
        }
    }
}
