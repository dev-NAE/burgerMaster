package com.itwillbs.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log
public class ManagerController {
    @GetMapping("/login")
    public String login(){
        log.info("AdminsController login()");
        return "managers/login";
    }

    @GetMapping("/managers")
    public String admins(){
        log.info("AdminsController managers()");

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
