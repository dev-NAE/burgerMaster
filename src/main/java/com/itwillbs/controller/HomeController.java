package com.itwillbs.controller;

import com.itwillbs.config.security.util.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String startPage(){

        if(Boolean.TRUE.equals(SecurityUtil.isAuthenticated())){
            return "redirect:/main";

        }else {
            return "redirect:/login";
        }
    }
}
