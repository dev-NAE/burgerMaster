package com.itwillbs.controller.manager;

import com.itwillbs.entity.Manager;
import com.itwillbs.repository.ManagerRepository;
import com.itwillbs.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class ManagerRestController {
    private final ManagerService managerService;
    private final ManagerRepository managerRepository;

    @PostMapping(value="/bgmManager/create", produces = "application/text; charset=UTF-8")
    public String create(Manager manager) {
        log.info("ManagerRestController create()");

        return managerService.createManger(manager);
    }

    @PostMapping(value = "/bgmManager/check/id")
    public Boolean  checkId(String managerId) {
        log.info("ManagerRestController checkId()");
        boolean result = false;
        // false 중복 없음, true 중복 있음min
        result = managerService.checkManagerId(managerId);
        log.info("managerId : "+managerId);
        log.info("result : "+result);

        return result;
    }

    @PostMapping(value = "/manager/update")
    public String update(Manager manager) {
        log.info("ManagerRestController update()");
        String json = managerService.updateManager(manager);

        return json;
    }
    @PostMapping(value = "/manager/delete")
    public String delete(Manager manager) {
        log.info("ManagerRestController delete()");
        String json = null;

        return json;
    }
}
