package com.gianvittorio.reactor.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@AllArgsConstructor
public class ReactorException extends Throwable {

    private String message;
    private Throwable exception;
}
