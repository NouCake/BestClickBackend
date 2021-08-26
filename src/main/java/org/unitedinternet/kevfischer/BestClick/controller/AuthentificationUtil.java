package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.http.ResponseCookie;
import org.unitedinternet.kevfischer.BestClick.model.AuthRequest;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.database.User;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class AuthentificationUtil {

    private final static Duration sessionExpireDuration = Duration.ofDays(7);

    public static Session createNewSession(UUID userId) {
        Session session = new Session();
        session.setUser(new User(userId));
        session.setExpires(new Date(sessionExpireDuration.toMillis() + System.currentTimeMillis()));
        session.setSession(UUID.randomUUID().toString());
        return session;
    }

    public static ResponseCookie createCookieFromSession(Session session) {
        return createCookieFromSession(session, sessionExpireDuration.getSeconds());
    }

    public static ResponseCookie createCookieFromSession(Session session, long maxAge){
        return ResponseCookie.from("session", session.getSession())
                .httpOnly(true)
                .maxAge(maxAge)
                .sameSite("None")
                .secure(true)
                .build();
    }

    public static boolean authUser(UserAuthData authData, AuthRequest request){
        return authData.getPasswordHash().equals(request.getPassword());
    }

    public static boolean isSessionExpired(Session session){
        return session.getExpires().getTime() < System.currentTimeMillis();
    }

}
