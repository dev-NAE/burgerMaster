package com.itwillbs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.service.InventoryService;
import com.itwillbs.service.MainDashBoardService;


@Controller
@RequestMapping("/inven")
@Log
@RequiredArgsConstructor
public class InventoryController {

	// 공통된 View 경로를 상수로 정의
    private static final String VIEW_PATH = "inventory_management/";
    
    private final InventoryService inventoryService;
    
    
	//재고 조회
	@GetMapping("/inventoryList")
	public String inventoryList(Model model) {
		log.info("InventroyController inventoryList()");
		
		
		//DB에 저장된 재고들의 목록을 불러옴
		List<InventoryItemDTO> inventoryItemDTOs = inventoryService.getInventoryItems();
		
		//model에 저장
		
		
		
		return VIEW_PATH + "inventory/list";
	}
	
	//입고 등록
	@GetMapping("/incomingInsert")
	public String incomingInsert() {
		log.info("InventroyController incomingInsert()");
		
		return VIEW_PATH + "incoming/insert";
	}
	
	//입고 조회
	@GetMapping("/incomingList")
	public String incomingList() {
		log.info("InventroyController incomingList()");
		
		return VIEW_PATH + "incoming/list";
	}
	
	//출고 등록
	@GetMapping("/outgoingInsert")
	public String outgoingInsert() {
		log.info("InventroyController outgoingInsert()");
		
		return VIEW_PATH + "outgoing/insert";
	}
	
	//출고 조회
	@GetMapping("/outgoingList")
	public String outgoingList() {
		log.info("InventroyController outgoingList()");
		
		return VIEW_PATH + "outgoing/list";
	}
	
}
