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
public class ManagerService {

    private final ManagerRepository mangerRepository;

    public Manager getAdminByAdminId(final String adminId) {
        Optional<Manager> manager;
        manager = mangerRepository.findById(adminId);
        if (manager.isPresent()) {
            return manager.get();
        }
        log.info("Admin not found");
        return null;
    }
}
