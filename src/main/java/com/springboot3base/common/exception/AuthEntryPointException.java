package com.springboot3base.common.exception;

public class AuthEntryPointException extends RuntimeException{
    public AuthEntryPointException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthEntryPointException(String message) {
        super(message);
    }

    public AuthEntryPointException() {
        super();
    }
}
