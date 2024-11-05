package com.itwillbs.service;

import com.itwillbs.entity.Manager;
import com.itwillbs.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log
public class SecurityService {
    private final ManagerRepository managerRepository;

    public Manager getManagerByManagerId(final String managerId) {
        Optional<Manager> manager;
        manager = managerRepository.findById(managerId);

        if (manager.isPresent()) {
            return manager.get();
        }
        log.info("Manager not found");
        return null;
    }
}
