package com.itwillbs.service;

import com.itwillbs.entity.Admin;
import com.itwillbs.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log
public class AdminService {

    private final AdminRepository adminRepository;

    public Admin getAdminByAdminId(final String adminId) {
        Optional<Admin> admin;
        admin = adminRepository.findById(adminId);
        if (admin.isPresent()) {
            return admin.get();
        }
        log.info("Admin not found");
        return null;
    }
}
