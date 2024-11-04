package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.repository.BOMRepository;

@Service
public class BOMService {
    private final BOMRepository bomRepository ;

    public BOMService(BOMRepository bomRepository) {
        this.bomRepository = bomRepository;
    }

}