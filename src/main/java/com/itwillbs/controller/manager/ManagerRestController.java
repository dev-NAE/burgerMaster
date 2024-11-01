package com.itwillbs.controller.manager;

import com.itwillbs.entity.Manager;
import com.itwillbs.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class ManagerRestController {
    private final ManagerService managerService;

    @PostMapping(value="/manager/create", produces = "application/text; charset=UTF-8")
    public String create(Manager manager) {
        log.info("ManagerRestController create()");

        String json = managerService.createManger(manager);
        log.info(manager.toString());

        return json;
    }

    @PostMapping(value = "/manager/check/id")
    public Boolean  checkId(String managerId) {
        log.info("ManagerRestController checkId()");
        boolean result = false;
        // false 중복 없음, true 중복 있음min
        result = managerService.checkManagerId(managerId);
        log.info("managerId : "+managerId);
        log.info("result : "+result);

        return result;
    }
}
