package com.itwillbs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.repository.IncomingRepository;
import com.itwillbs.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@Service
@RequiredArgsConstructor
@Log
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final IncomingRepository incomingRepository;
    
    // 재고 전체 조회 (페이지네이션 지원)
    public Page<InventoryItemDTO> getInventoryItems(Pageable pageable) {
        log.info("getInventoryItems()");
        return inventoryRepository.getAllInventoryItems(pageable);
    }

    // 재고 부족 품목만 조회 (검색 조건 포함)
    public Page<InventoryItemDTO> findInventoryItemsByOutOfStock(String itemCodeOrName, String itemType, Pageable pageable) {
        log.info("findInventoryItemsByOutOfStock()");
        return inventoryRepository.findInventoryItemsByOutOfStock(itemCodeOrName, itemType, pageable);
    }

    // 재고 검색 (검색 조건과 페이지네이션)
    public Page<InventoryItemDTO> findInventoryItems(String itemCodeOrName, String itemType, Pageable pageable) {
        log.info("findInventoryItems()");
        return inventoryRepository.findInventoryItems(itemCodeOrName, itemType, pageable);
    }

    // 입고 전체 조회
	public Page<IncomingDTO> getIncomingLists(Pageable pageable) {
		log.info("getIncomingLists()");
		
		return incomingRepository.getAllIncomingLists(pageable);
    }
}
