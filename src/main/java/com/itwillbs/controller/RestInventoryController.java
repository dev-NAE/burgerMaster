package com.itwillbs.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/inven")
@Log
@RequiredArgsConstructor
public class RestInventoryController {
	
	// 공통된 View 경로를 상수로 정의
    private static final String VIEW_PATH = "inventory_management/";
    
    private final InventoryService inventoryService;
    
	//재고 조회 ajax 비동기 검색
	@GetMapping("/filterInventory")
	public ResponseEntity<List<InventoryItemDTO>> filterInventory(
	        @RequestParam(required = false) String itemCodeOrName,
	        @RequestParam(required = false) String itemType,
	        @RequestParam(defaultValue = "false") boolean findOutOfStock) {

		List<InventoryItemDTO> filteredItems = null;
		
		
		if(findOutOfStock == true) {
			//findOutOfStock(재고부족품목만 찾기)의 값이 true면? -> (재고량 < 최소필요재고량인 품목만 조회)
			filteredItems = inventoryService.findInventoryItemOnlyOutOfStock();
		}else {
			//false면? -> 검색한 품목 조회
			filteredItems = inventoryService.findInventoryItem(itemCodeOrName, itemType);
		}
	    return ResponseEntity.ok(filteredItems);
	}
}
