package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.unitedinternet.kevfischer.BestClick.model.AuthRequest;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.database.User;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAppData;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;
import org.unitedinternet.kevfischer.BestClick.model.repository.SessionRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAuthRepository;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/me")
public class AuthedUserController {

    @Autowired private UserAuthRepository authRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private UserAppRepository appRepository;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, @CookieValue(required = false) String oldSessionCookie){
        UserAuthData auth = ControllerUtil.getOptionalOrThrow(authRepository.findByUsername(request.getUsername()));

        if(!auth.getPasswordHash().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Duration sessionExpireDuration = Duration.ofDays(7);

        Session session = new Session();
        session.setUser(auth.getUser());
        session.setExpires(new Date(System.currentTimeMillis() + sessionExpireDuration.toMillis()));
        session.setSession(UUID.randomUUID().toString());

        sessionRepository.save(session);

        ResponseCookie cookies = ResponseCookie.from("session", session.getSession())
                .httpOnly(true)
                .maxAge(sessionExpireDuration.getSeconds())
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookies.toString()).build();
    }

    @PostMapping("/add")
    public void addCounter(@CookieValue String session){
        User user = getUserFromSession(session);
        if(user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        UserAppData appData = user.getAppData();
        appData.setCounter(appData.getCounter()+1);

        appRepository.save(appData);
    }

    private User getUserFromSession(String session){
        var oSession = sessionRepository.findBySession(session);
        if(oSession.isEmpty()) {
            return null;
        }

        Session ses = oSession.get();
        if(ses.getExpires().getTime() < System.currentTimeMillis()) {
            sessionRepository.delete(ses);
            return null;
        }

        return ses.getUser();
    }

}
