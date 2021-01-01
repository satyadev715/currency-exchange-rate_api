package com.currencyexchangeratetracker.filter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestDateException extends RuntimeException {
    public RequestDateException(String message) {
        super(message);
    }
}