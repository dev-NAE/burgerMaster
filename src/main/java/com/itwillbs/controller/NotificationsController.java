package com.itwillbs.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.domain.dashboard.DefectiveDTO;
import com.itwillbs.domain.dashboard.IncomingItemDTO;
import com.itwillbs.domain.dashboard.InventoryItemDTO;
import com.itwillbs.domain.dashboard.ItemDashDTO;
import com.itwillbs.entity.Item;
import com.itwillbs.entity.dashboard.IncommingItem;
import com.itwillbs.entity.dashboard.Sale;
import com.itwillbs.service.MainDashBoardService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@RestController
public class NotificationsController {

	private final MainDashBoardService mainDashBoardService;
	
//	@GetMapping("/notifications")
//	public Map<String, Object> getNotifications() {
	    // 알람 데이터를 가져옵니다.
//	    List<InventoryItemDTO> itemList = mainDashBoardService.getItemList(); // 모든 아이템 리스트 가져오기
//	    List<String> notifications = new ArrayList()<>(); // 알람을 저장할 리스트
//	    
//	    // 수량이 5개 이하인 아이템만 알람에 추가
//	    for (InventoryItemDTO item : itemList) {
//	        if (item.getQuantity() <= 5) {
//	            notifications.add(item.getItemName() + "의 수량이 " + item.getQuantity() + "개입니다."); // 알람 메시지 추가
//	        }
//	    }
//	    
//	    int notificationCount = notifications.size(); // 알람 수
//
//	    // 결과를 맵으로 반환합니다.
//	    Map<String, Object> response = new HashMap<>();
//	    response.put("count", notificationCount);
//	    response.put("notifications", notifications);
//
//	    return response; // JSON 형태로 반환
//	}
	
	
	
	
	
}//
