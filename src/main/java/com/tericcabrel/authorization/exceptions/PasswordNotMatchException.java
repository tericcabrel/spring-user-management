package com.tericcabrel.authorization.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordNotMatchException extends Exception{

    private static final long serialVersionUID = 1L;

    public PasswordNotMatchException(String message){
        super(message);
    }
}