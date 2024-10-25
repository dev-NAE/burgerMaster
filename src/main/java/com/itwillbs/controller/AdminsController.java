package com.itwillbs.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log
public class AdminsController {
    @GetMapping("/login")
    public String login(){
        log.info("AdminsController login()");
        return "admins/login";
    }

    @GetMapping("/admins")
    public String admins(){
        log.info("AdminsController admins()");

        return "admins/admins";
    }
}
