package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.repository.ItemRepository;

@Service
public class FranchiseService {
    private final ItemRepository itemRepository;

    public FranchiseService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

}