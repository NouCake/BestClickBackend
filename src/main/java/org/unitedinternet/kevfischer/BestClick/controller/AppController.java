package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.database.User;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAppData;
import org.unitedinternet.kevfischer.BestClick.model.repository.SessionRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired private UserAppRepository appRepository;
    @Autowired private SessionRepository sessionRepository;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void add(HttpServletRequest request){
        Session session = AuthentificationUtil.auth(sessionRepository, request);
        appRepository.incrCounter(1, session.getUser().getId().toString());
    }

}
