package com.itwillbs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.entity.Manager;
import com.itwillbs.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public Manager getManagerByManagerId(final String managerId) {
        Optional<Manager> manager;
        manager = managerRepository.findById(managerId);

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

    public Page<Manager> getManagerList(Pageable pageable, String search) {
        log.info("ManagerService Getting manager list");
        if(search.equals("") || search == null) {
            return  managerRepository.findAll(pageable);
        }
        return  managerRepository.findBySearch(pageable,search);
    }

    public boolean checkManagerId(String managerId) {
        log.info("ManagerService Checking manager");
        boolean result = false;

        log.info("manage DB : "+managerRepository.findById(managerId).isPresent());

        if(managerRepository.findById(managerId).isPresent()) {
            result = true;
        }
        return result;
    }

    public String updateManager(Manager manager) {
        log.info("ManagerService Updating manager");
        ObjectMapper objectMapper = new ObjectMapper();
        Manager managerDB = getManagerByManagerId(manager.getManagerId());
        String json = null;



        if(managerDB != null) {
            if(manager.getPass().isEmpty()) {
                manager.setPass(managerDB.getPass());
            }else{
                manager.setPass(bCryptPasswordEncoder.encode(manager.getPass()));
            }


            managerRepository.save(manager);
            // 권한 리스트
            String[] managerRoles = manager.getManagerRole().split(",");
            ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for(String role : managerRoles){
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
            log.info("updateManager : " + Arrays.toString(managerRoles));

            try {
                json = objectMapper.writeValueAsString(manager);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return json;
    }
}
