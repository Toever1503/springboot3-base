package com.springboot3base.common.exception;

public class AuthFailedException extends RuntimeException{
    public AuthFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthFailedException(String message) {
        super(message);
    }

    public AuthFailedException() {
        super();
    }
}
