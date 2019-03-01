package com.app.katchup.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GenericException extends Throwable {
    public GenericException(String exception) {
        super(exception);
    }
}
