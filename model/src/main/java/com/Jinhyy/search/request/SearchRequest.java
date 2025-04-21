package com.Jinhyy.search.request;

import com.Jinhyy.search.type.SortType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class SearchRequest {

    @NotBlank
    private String query;
    private SortType sortType;

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer pageSize;
}
