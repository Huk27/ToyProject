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
public class NaverBaseResponse {
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("message")
    private String message;
}
