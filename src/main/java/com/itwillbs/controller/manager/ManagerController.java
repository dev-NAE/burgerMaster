package com.itwillbs.controller.manager;

import com.itwillbs.config.security.util.SecurityUtil;
import com.itwillbs.entity.Manager;
import com.itwillbs.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class ManagerController {
    private final ManagerService managerService;


    @GetMapping("/login")
    public String login(){
        log.info("AdminsController login()");
        return "managers/login";
    }

    @GetMapping("/manager/list")
    public String managerList(Model model){
        log.info("AdminsController managers()");
        log.info("isAuthenticated : "+SecurityContextHolder.getContext()
                .getAuthentication().isAuthenticated());
        log.info("authorities : "+SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities());
        log.info("security util name : " + SecurityUtil.getUserId());

        List<Manager> managers = managerService.getManagerList();

        model.addAttribute("managers", managers);

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
