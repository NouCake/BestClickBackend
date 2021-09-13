package org.unitedinternet.kevfischer.BestClick.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.server.ResponseStatusException;
import org.unitedinternet.kevfischer.BestClick.controller.service.InsideLoginService;
import org.unitedinternet.kevfischer.BestClick.model.AuthRequest;
import org.unitedinternet.kevfischer.BestClick.model.ProviderInformation;
import org.unitedinternet.kevfischer.BestClick.model.Ticket;
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

    public static class UserNotRegisteredException extends RuntimeException {
        private ProviderInformation info;

        public UserNotRegisteredException(ProviderInformation info) {
            this.info = info;
        }

        public ProviderInformation getInfo() {
            return info;
        }

        public void setInfo(ProviderInformation info) {
            this.info = info;
        }
    }

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

    public static User authUser(InsideLoginService service, RedisCache cache, UserAuthRepository authRepository, AuthRequest request) throws ResponseStatusException{
        if(request.getUsername() != null && request.getPassword() != null) {
            UserAuthData authData = ControllerUtil.getOptionalOrThrowStatus(authRepository.findByUsername(request.getUsername().toLowerCase()), HttpStatus.NOT_FOUND, "username or passowrd wrong");
            if(!BCrypt.checkpw(request.getPassword(), authData.getPasswordHash())) return null;
            return new User(authData.getUserId(), null, null, authData);
        } else if(request.getTicket() != null) {
            return authTicket(authRepository, service,cache, request);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad auth method");
    }

    private static User authTicket(UserAuthRepository authRepository, InsideLoginService service, RedisCache cache, AuthRequest request){
        Ticket ticket = cache.getTicket(request.getTicket());

        if(ticket == null || ticket.getStatus() == Ticket.STATUS.DONE) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no ticket/already done");
        if(ticket.getStatus() == Ticket.STATUS.WAITING) throw new ResponseStatusException(HttpStatus.TOO_EARLY, "ticket waiting");

        if(ticket.getProvider() == Ticket.PROVIDER.INSIDE)
            return authInsideTicket(authRepository, cache, service, ticket);

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static User authInsideTicket(UserAuthRepository authRepository, RedisCache cache, InsideLoginService service, Ticket ticket){
        try {
            ProviderInformation providerInfo = service.authentificateTicket(ticket);
            if(providerInfo == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Ticket");
            var oAuthData = authRepository.findByInsideId(providerInfo.getProviderId());

            if(oAuthData.isEmpty()) {
                ticket.setInformation(providerInfo);
                cache.cache(ticket);
                throw new UserNotRegisteredException(providerInfo);
            }
            UserAuthData authData = oAuthData.get();
            return new User(authData.getUserId(), null, null, authData);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "jackson error");
        }
    }

    public static boolean isSessionExpired(Session session){
        return session.getExpires().getTime() < System.currentTimeMillis();
    }

}
