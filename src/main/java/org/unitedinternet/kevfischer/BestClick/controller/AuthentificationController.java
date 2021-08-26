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
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;
import org.unitedinternet.kevfischer.BestClick.model.repository.SessionRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAuthRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class AuthentificationController {


    @Autowired private SessionRepository sessionRepository;
    @Autowired private UserAuthRepository authRepository;

    @GetMapping("/")
    public @ResponseBody Session auth(HttpServletRequest request){
        String sessionId = ControllerUtil.getCookieOrThrowStatus(request, "session", HttpStatus.BAD_REQUEST);
        Session session = ControllerUtil.getOptionalOrThrowStatus(sessionRepository.findBySession(sessionId), HttpStatus.UNAUTHORIZED);
        if(AuthentificationUtil.isSessionExpired(session)) {
            sessionRepository.delete(session);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        session.getUser().getProfile().getName();
        return session;
    }

    @PostMapping("/login")
    public ResponseEntity<Session> login(@RequestBody AuthRequest request){
        UserAuthData authData = ControllerUtil.getOptionalOrThrowStatus(authRepository.findByUsername(request.getUsername().toLowerCase()), HttpStatus.NOT_FOUND);

        if(!AuthentificationUtil.authUser(authData, request)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Session session = AuthentificationUtil.createNewSession(authData.getUserId());
        sessionRepository.save(session);
        ResponseCookie cookie = AuthentificationUtil.createCookieFromSession(session);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        Session session = auth(request);
        sessionRepository.delete(session);
        ResponseCookie cookie = AuthentificationUtil.createCookieFromSession(session, 0); //delete cookie
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

}
