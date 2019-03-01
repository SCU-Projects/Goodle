package com.app.katchup.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotAcceptableException extends Throwable {
    public NotAcceptableException(String exception) {
        super(exception);
    }
}
