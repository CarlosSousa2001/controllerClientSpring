package com.client.controllerClienteMvc.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundExceptionPersonalized extends RuntimeException {
    public EntityNotFoundExceptionPersonalized(String s) {
        super(s);
    }
}
