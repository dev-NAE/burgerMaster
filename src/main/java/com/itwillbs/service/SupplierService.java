package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.repository.SupplierRepository;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

}