package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.access.HandlerMappingIntrospectorRequestTransformer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.domain.dashboard.DefectiveDTO;
import com.itwillbs.domain.dashboard.IncomingItemDTO;
import com.itwillbs.domain.dashboard.InventoryItemDTO;
import com.itwillbs.entity.dashboard.SaleDash;
import com.itwillbs.service.MainDashBoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@Log
@RequiredArgsConstructor
public class NotificationController {

	private final MainDashBoardService mainDashBoardService;

	@GetMapping("/noticontent")
	@ResponseBody
    public List<InventoryItemDTO> getInventoryItems() {
        log.info("NotificationController: Fetching inventory items");

        // 원재료 재고 조회
        List<InventoryItemDTO> rmInventoryList = mainDashBoardService.getItemInventory("RM");
        log.info("RM Inventory List: " + rmInventoryList);

        // 완재품 재고 조회
        List<InventoryItemDTO> fpInventoryList = mainDashBoardService.getItemInventory("FP");
        log.info("FP Inventory List: " + fpInventoryList);

        // 가공품 재고 조회
        List<InventoryItemDTO> ppInventoryList = mainDashBoardService.getItemInventory("PP");
        log.info("PP Inventory List: " + ppInventoryList);

        // 원재료, 완제품, 가공품 리스트 합치기
        List<InventoryItemDTO> notifiList = new ArrayList<>();
        notifiList.addAll(mainDashBoardService.getItemInventory("RM"));
        notifiList.addAll(mainDashBoardService.getItemInventory("FP"));
        notifiList.addAll(mainDashBoardService.getItemInventory("PP"));
        System.out.println("notifiList"+notifiList);
      
        
   
        
        return notifiList; // JSON 형식으로 반환
    }

}//
