package com.itwillbs.controller;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.service.InventoryService;

@Controller
@RequestMapping("/inven")
@Log
@RequiredArgsConstructor
public class InventoryController {

	// 공통된 View 경로를 상수로 정의
    private static final String VIEW_PATH = "inventory_management/";
    
    private final InventoryService inventoryService;
    
    
    @GetMapping("/inventoryList")
    public String inventoryList(
            @RequestParam(name = "itemCodeOrName", required = false) String itemCodeOrName,
            @RequestParam(name = "itemType", required = false) String itemType,
            @RequestParam(name = "findOutOfStock", defaultValue = "false") boolean findOutOfStock,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        log.info("InventoryController inventoryList()");

        // Pageable 객체를 생성하여 페이지 정보를 설정
        Pageable pageable = PageRequest.of(page, size);
        // 검색 조건과 페이지 정보를 사용하여 재고 조회
//        Page<InventoryItemDTO> inventoryItemDTOs = inventoryService.findInventoryItems(itemCodeOrName, itemType, findOutOfStock, pageable);

        // 모델에 조회된 재고 데이터를 저장
//        model.addAttribute("inventoryItemDTOs", inventoryItemDTOs);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", inventoryItemDTOs.getTotalPages());
//        model.addAttribute("itemCodeOrName", itemCodeOrName);
//        model.addAttribute("itemType", itemType);
//        model.addAttribute("findOutOfStock", findOutOfStock);

        return VIEW_PATH + "inventory_list";
    }
	

		
		

	
	//입고 등록
	@GetMapping("/incomingInsert")
	public String incomingInsert() {
		log.info("InventroyController incomingInsert()");
		
		return VIEW_PATH + "incoming_insert";
	}
	
	//입고 조회
	@GetMapping("/incomingList")
	public String incomingList() {
		log.info("InventroyController incomingList()");
		
		return VIEW_PATH + "incoming_list";
	}
	
	//출고 등록
	@GetMapping("/outgoingInsert")
	public String outgoingInsert() {
		log.info("InventroyController outgoingInsert()");
		
		return VIEW_PATH + "outgoing_insert";
	}
	
	//출고 조회
	@GetMapping("/outgoingList")
	public String outgoingList() {
		log.info("InventroyController outgoingList()");
		
		return VIEW_PATH + "outgoing_list";
	}
	
}
