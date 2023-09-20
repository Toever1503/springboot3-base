package com.springboot3base.common.exception;

public class AuthExpireException extends RuntimeException{
    public AuthExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthExpireException(String message) {
        super(message);
    }

    public AuthExpireException() {
    }
}
