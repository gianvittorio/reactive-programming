package com.gianvittorio.reactor.exception;

import lombok.Getter;

@Getter
public class NetworkException extends RuntimeException{

    private String message;

    public NetworkException(String message) {
        super(message);
        this.message = message;
    }

    public NetworkException(Throwable t) {
        super(t);
    }
}
