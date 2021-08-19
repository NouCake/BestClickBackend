package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class ControllerUtil {

    public static <T> T getOptionalOrThrow(Optional<T> optional, HttpStatus status){
        if(optional.isEmpty()) throw new ResponseStatusException(status);
        return optional.get();
    }

    public static <T> T getOptionalOrThrow(Optional<T> optional){
        return getOptionalOrThrow(optional, HttpStatus.NOT_FOUND);
    }

}
