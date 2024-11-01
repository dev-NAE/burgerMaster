package com.itwillbs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.entity.Manager;
import com.itwillbs.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Manager getAdminByAdminId(final String adminId) {
        Optional<Manager> manager;
        manager = managerRepository.findById(adminId);

        if (manager.isPresent()) {
            return manager.get();
        }
        log.info("Admin not found");
        return null;
    }

    public String createManger(Manager manager) {
        log.info("ManagerService Creating manager");
        ObjectMapper objectMapper = new ObjectMapper();
        Manager encManger = Manager.createManger(manager, bCryptPasswordEncoder);
        managerRepository.save(encManger);

        String result = null;
        try {
            result = objectMapper.writeValueAsString(encManger);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Manager> getManagerList() {
        log.info("ManagerService Getting manager list");

        List<Manager> managers = null;
        managers = managerRepository.findAll();

        return managers;
    }

    public boolean checkManagerId(String managerId) {
        log.info("ManagerService Checking manager");
        boolean result = false;

        log.info("manage DB : "+managerRepository.findById(managerId));
        if(managerRepository.findById(managerId).get() != null) {
            result = true;
        }
        return result;
    }
}
