package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.redis.RedisCache;
import org.unitedinternet.kevfischer.BestClick.model.repository.SessionRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired private UserAppRepository appRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private RedisCache redisCache;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody int add(HttpServletRequest request, @RequestParam(defaultValue = "1") int counter){
        Session session = AuthentificationUtil.auth(redisCache, sessionRepository, request);
        return appRepository.incrCounter(counter, session.getUser().getId().toString());
    }

}
