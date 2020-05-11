package com.mcommandes.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CommandeAlreadyPaiedException extends RuntimeException {


    public CommandeAlreadyPaiedException(String message) {
        super(message);
    }
}
