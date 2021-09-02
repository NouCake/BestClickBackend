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
import org.unitedinternet.kevfischer.BestClick.model.database.User;
import org.unitedinternet.kevfischer.BestClick.model.redis.Session;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;
import org.unitedinternet.kevfischer.BestClick.model.repository.RSessionRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAuthRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserProfileRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthentificationController {


    @Autowired private RSessionRepository sessionRepository;
    @Autowired private UserAuthRepository authRepository;
    @Autowired private UserProfileRepository profileRepository;
    @Autowired private UserAppRepository appRepository;

    @GetMapping("/")
    public @ResponseBody org.unitedinternet.kevfischer.BestClick.model.database.Session auth(HttpServletRequest request){
        Session rSession = AuthentificationUtil.auth(sessionRepository, request);

        org.unitedinternet.kevfischer.BestClick.model.database.Session dbSession = new org.unitedinternet.kevfischer.BestClick.model.database.Session();
        User user = new User();
        user.setId(rSession.getUserId());
        user.setProfile(ControllerUtil.getOptionalOrThrowStatus(profileRepository.findById(rSession.getUserId())));
        user.setAppData(ControllerUtil.getOptionalOrThrowStatus(appRepository.findById(rSession.getUserId())));
        dbSession.setUser(user);
        dbSession.setSession(rSession.getSession());
        dbSession.setExpires(rSession.getExpires());
        return dbSession;
    }

    @PostMapping("/login")
    public ResponseEntity<Session> login(@RequestBody AuthRequest request){
        UserAuthData authData = ControllerUtil.getOptionalOrThrowStatus(authRepository.findByUsername(request.getUsername().toLowerCase()), HttpStatus.NOT_FOUND);

        if(!AuthentificationUtil.authUser(authData, request)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Session session = AuthentificationUtil.createNewSession(authData.getUserId());
        //sessionRepository.saveSession(session.getExpires(), session.getUser().getId(), session.getSession());
        sessionRepository.save(session);
        ResponseCookie cookie = AuthentificationUtil.createCookieFromSession(session);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String session = auth(request).getSession().toString();
        sessionRepository.deleteById(UUID.fromString(session));
        ResponseCookie cookie = AuthentificationUtil.createCookieFromSession(session, 0); //delete cookie
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

}
