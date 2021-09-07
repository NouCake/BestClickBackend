package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.server.ResponseStatusException;
import org.unitedinternet.kevfischer.BestClick.model.AuthRequest;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.database.User;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;
import org.unitedinternet.kevfischer.BestClick.model.redis.RedisCache;
import org.unitedinternet.kevfischer.BestClick.model.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class AuthentificationUtil {


    public static Session auth(RedisCache cache, SessionRepository sessionRepository, HttpServletRequest request) throws ResponseStatusException{
        String sessionId = ControllerUtil.getCookieOrThrowStatus(request, "session", HttpStatus.BAD_REQUEST);
        Session session = cache.getSession(UUID.fromString(sessionId));
        if(session == null) {
            session = ControllerUtil.getOptionalOrThrowStatus(sessionRepository.findById(UUID.fromString(sessionId)), HttpStatus.UNAUTHORIZED);
            cache.cache(session);
        }

        if(AuthentificationUtil.isSessionExpired(session)) {
            sessionRepository.delete(session);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return session;
    }

    private final static Duration sessionExpireDuration = Duration.ofDays(7);

    public static Session createNewSession(UUID userId) {
        Session session = new Session();
        session.setUser(new User(userId));
        session.setExpires(new Date(sessionExpireDuration.toMillis() + System.currentTimeMillis()));
        session.setSession(UUID.randomUUID());
        return session;
    }

    public static ResponseCookie createCookieFromSession(Session session) {
        return createCookieFromSession(session.getSession().toString(), sessionExpireDuration.getSeconds());
    }

    public static ResponseCookie createCookieFromSession(String session, long maxAge){
        return ResponseCookie.from("session", session)
                .httpOnly(true)
                .maxAge(maxAge)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
    }

    public static boolean authUser(UserAuthData authData, AuthRequest request){
        return BCrypt.checkpw(request.getPassword(), authData.getPasswordHash());
    }

    public static boolean isSessionExpired(Session session){
        return session.getExpires().getTime() < System.currentTimeMillis();
    }

}
