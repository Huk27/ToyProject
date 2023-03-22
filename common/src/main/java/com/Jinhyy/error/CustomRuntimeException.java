package com.Jinhyy.error;

import lombok.Getter;

public class CustomRuntimeException extends RuntimeException {
    Integer statusCode;
    String message;

    public CustomRuntimeException(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static CustomRuntimeException EXCEPTION_REQUEST_ARGUMENT_INVALID() {
        return new CustomRuntimeException(400, "request.argument.invalid");
    }
}
