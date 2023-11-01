package com.client.controllerClienteMvc.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameUniqueViolationException extends RuntimeException {
    public UsernameUniqueViolationException(String s) {
        super(s);
    }
}
