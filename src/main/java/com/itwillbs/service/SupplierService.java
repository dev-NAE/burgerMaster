package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.repository.ItemRepository;

@Service
public class SupplierService {
    private final ItemRepository itemRepository;

    public SupplierService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

}