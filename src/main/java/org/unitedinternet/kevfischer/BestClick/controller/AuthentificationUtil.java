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
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAuthRepository;

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

    public static User authUser(RedisCache cache, UserAuthRepository authRepository, AuthRequest request) throws ResponseStatusException{
        if(request.getUsername() != null && request.getPassword() != null) {
            UserAuthData authData = ControllerUtil.getOptionalOrThrowStatus(authRepository.findByUsername(request.getUsername().toLowerCase()), HttpStatus.NOT_FOUND);
            if(!BCrypt.checkpw(request.getPassword(), authData.getPasswordHash())) return null;
            return new User(authData.getUserId(), null, null, authData);
        } else if(request.getTicket() != null) {
            return authTicket(cache, request);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad auth method");
    }

    private static User authTicket(RedisCache cache, AuthRequest request){
        String key = String.join(":", "ticket", request.getTicket());
        String ticketValue = cache.getObject(key);

        if(ticketValue == null || "done".equals(ticketValue)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no ticket/already done");
        if("waiting".equals(ticketValue)) throw new ResponseStatusException(HttpStatus.TOO_EARLY, "ticket waiting");

        String[] parts = ticketValue.split(" ");
        if(parts.length < 2) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        if(parts[0].equals("INSIDE")) return authInsideTicket(parts[1]);
        //else if(parts[1].equals("~~~~")) return null;

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static User authInsideTicket(String insideTicket){
        
        return null;
    }

    public static boolean isSessionExpired(Session session){
        return session.getExpires().getTime() < System.currentTimeMillis();
    }

}
