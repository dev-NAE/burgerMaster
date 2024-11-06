package com.itwillbs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@Service
@RequiredArgsConstructor
@Log
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    // 재고 전체 조회 (페이지네이션 지원)
    public List<InventoryItemDTO> getInventoryItems() {
        log.info("getInventoryItems()");
        return inventoryRepository.getAllInventoryItems();
    }

    // 재고 부족 품목 조회 (검색 조건 포함)
    public List<InventoryItemDTO> findInventoryItemsByOutOfStock(String itemCodeOrName, String itemType) {
        log.info("findInventoryItemsByOutOfStock()");
        return inventoryRepository.findInventoryItemsByOutOfStock(itemCodeOrName, itemType);
    }

    // 재고 검색 (검색 조건과 페이지네이션)
    public List<InventoryItemDTO> findInventoryItems(String itemCodeOrName, String itemType) {
        log.info("findInventoryItems()");
        return inventoryRepository.findInventoryItems(itemCodeOrName, itemType);
    }
}