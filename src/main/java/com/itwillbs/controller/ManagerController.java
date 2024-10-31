package com.itwillbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.entity.Manager;
import com.itwillbs.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Log
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping(value="/manager/create", produces = "application/text; charset=UTF-8")
    @ResponseBody
    public String create(Manager manager) {
        log.info("ManagerController create()");

        String json = managerService.createManger(manager);;
        log.info(manager.toString());

        return json;
    }
    @GetMapping("/login")
    public String login(){
        log.info("AdminsController login()");
        return "managers/login";
    }

    @GetMapping("/manager/list")
    public String admins(){
        log.info("AdminsController managers()");
        log.info("isAuthenticated : "+SecurityContextHolder.getContext()
                .getAuthentication().isAuthenticated());
        log.info("authorities : "+SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities());
        log.info("name : "+SecurityContextHolder.getContext()
                .getAuthentication().getName());
        return "managers/managers";
    }
    @GetMapping("error/403")
    public String error403() {
        return "/error/403error";
    }

    @GetMapping("error/401")
    public String error401() {
        return "/error/401error";
    }
}
