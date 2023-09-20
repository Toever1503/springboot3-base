package com.springboot3base.common.exception;

import lombok.Data;

@Data
public class CommonMessageException extends RuntimeException{

    private int code;

    public CommonMessageException(int code) {
        this.code = code;
    }
}
