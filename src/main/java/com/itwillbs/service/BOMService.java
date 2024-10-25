package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.repository.ItemRepository;

@Service
public class BOMService {
    private final ItemRepository itemRepository;

    public BOMService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

}