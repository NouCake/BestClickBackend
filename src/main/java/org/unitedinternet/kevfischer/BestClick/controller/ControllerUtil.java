package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class ControllerUtil {

    public static <T> T getOptionalOrThrowStatus(Optional<T> optional, HttpStatus status){
        if(optional.isEmpty()) throw new ResponseStatusException(status);
        return optional.get();
    }

    public static <T> T getOptionalOrThrowStatus(Optional<T> optional){
        return getOptionalOrThrowStatus(optional, HttpStatus.NOT_FOUND);
    }

    public static String getCookieOrThrowStatus(HttpServletRequest request, String name, HttpStatus status) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if(cookie == null) {
            throw new ResponseStatusException(status);
        }
        return cookie.getValue();
    }

}
