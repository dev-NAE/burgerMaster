package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.repository.FranchiseRepository;

@Service
public class FranchiseService {
    private final FranchiseRepository franchiseRepository;

    public FranchiseService(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

}