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
public class NaverExternalSearchRequest {

    protected String query;
    protected SortType sortType;
    protected Integer start;
    protected Integer display;

    public void verifyAndAdjust() {
        if (Is.Negative(start) || Is.Negative(display) || (query != null && query.isBlank())) {
            throw CustomRuntimeException.EXCEPTION_REQUEST_ARGUMENT_INVALID();
        }

        if (start > 1000) {
            start = 1000;
        }

        if (display > 100) {
            display = 100;
        }
    }

    public NaverExternalSearchRequest setStart(int page, int pageSize) {
        this.start = page * pageSize;
        return this;
    }
}
