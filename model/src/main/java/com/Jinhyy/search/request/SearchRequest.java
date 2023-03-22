package com.Jinhyy.search.request;

import com.Jinhyy.search.type.SortType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
