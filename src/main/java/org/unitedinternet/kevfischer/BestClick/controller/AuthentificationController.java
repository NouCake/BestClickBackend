package org.unitedinternet.kevfischer.BestClick.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.unitedinternet.kevfischer.BestClick.controller.service.InsideLoginService;
import org.unitedinternet.kevfischer.BestClick.model.AuthRequest;
import org.unitedinternet.kevfischer.BestClick.model.Ticket;
import org.unitedinternet.kevfischer.BestClick.model.database.User;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.redis.RedisCache;
import org.unitedinternet.kevfischer.BestClick.model.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.time.Duration;

@Controller
@RequestMapping("/auth")
public class AuthentificationController {


    @Autowired private RedisCache redisCache;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private UserAuthRepository authRepository;
    @Autowired private UserProfileRepository profileRepository;
    @Autowired private UserAppRepository appRepository;
    @Autowired private InsideLoginService insideService;

    @GetMapping("/")
    public @ResponseBody Session auth(HttpServletRequest request){
        Session session = AuthentificationUtil.auth(redisCache, sessionRepository, request);

        User user = session.getUser();
        user.setProfile(ControllerUtil.getOptionalOrThrowStatus(profileRepository.findById(user.getId())));
        user.setAppData(ControllerUtil.getOptionalOrThrowStatus(appRepository.findById(user.getId())));
        return session;
    }

    @PostMapping("/login")
    public ResponseEntity<Session> login(@RequestBody AuthRequest request){
        User user = AuthentificationUtil.authUser(insideService, redisCache, authRepository, request);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "could not auth");
        }

        Session session = AuthentificationUtil.createNewSession(user.getId());
        sessionRepository.saveSession(session.getExpires(), session.getUser().getId(), session.getSession());
        ResponseCookie cookie = AuthentificationUtil.createCookieFromSession(session);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/login/ticket")
    public @ResponseBody String ticketInside(){
        Ticket ticket = new Ticket();
        ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setStatus(Ticket.STATUS.WAITING);
        redisCache.cache(ticket);

        return ticket.getTicketId();
    }

    @GetMapping("/login/inside")
    public void loginInside(@RequestParam String ticket, @RequestParam String bestTicket){
        Ticket cachedTicket = redisCache.getTicket(bestTicket);
        if(cachedTicket == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no ticket");
        cachedTicket.setProvider(Ticket.PROVIDER.INSIDE);
        cachedTicket.setStatus(Ticket.STATUS.GOT);

        redisCache.cache(cachedTicket);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String session = AuthentificationUtil.auth(redisCache, sessionRepository, request).getSession().toString();
        sessionRepository.deleteById(UUID.fromString(session));
        redisCache.uncache(UUID.fromString(session));
        ResponseCookie cookie = AuthentificationUtil.createCookieFromSession(session, 0); //delete cookie
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

}
