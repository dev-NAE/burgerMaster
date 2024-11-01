package com.itwillbs.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

@Controller
@Log
@RequiredArgsConstructor
public class MainDashBoardController {

	private final MainDashBoardService mainDashBoardService;
	
	@GetMapping("/main")
	public String main(org.springframework.ui.Model model) {
		log.info("MainDashBoardController main");
		
			//원재료 입고  
			List<IncomingItemDTO>rmItemList=mainDashBoardService.getItemIncomming("RM");
			System.out.println("MainDashBoardController rmItemList:"+rmItemList);
			model.addAttribute("rmItemList", rmItemList);
			
			//원재료 재고
			List<InventoryItemDTO>rmInventoryList=mainDashBoardService.getItemInventory("RM");
			System.out.println("MainDashBoardController rmInventoryList:"+rmInventoryList);
			model.addAttribute("rmInventoryList", rmInventoryList);
			
			//완재품 입고
			List<IncomingItemDTO>fpItemList=mainDashBoardService.getItemIncomming("FP");
			System.out.println("MainDashBoardController fpItemList:"+fpItemList);
			model.addAttribute("fpItemList", fpItemList);
			
			//완재품 재고
			List<InventoryItemDTO>fpInventoryList=mainDashBoardService.getItemInventory("FP");
			System.out.println("MainDashBoardController fpInventoryList:"+fpInventoryList);
			model.addAttribute("fpInventoryList", fpInventoryList);
			
			
			//가공품 입고 
//			List<IncomingItemDTO>ppItemList=mainDashBoardService.getItemIncomming("PP");
//			System.out.println("MainDashBoardController ppItemList:"+ppItemList);
//			model.addAttribute("ppItemList", ppItemList);
			
			//가공품 재고
			List<InventoryItemDTO>ppInventoryList=mainDashBoardService.getItemInventory("PP");
			System.out.println("MainDashBoardController ppInventoryList:"+ppInventoryList);
			model.addAttribute("ppInventoryList", ppInventoryList);
			
			
			
			//반품률
			List<DefectiveDTO>dfItemList=mainDashBoardService.findByItemSatus("반품");
			System.out.println("MainDashBoardController dfItemList:"+dfItemList);
			model.addAttribute("dfItemList", dfItemList);
			
			//폐기율
			List<DefectiveDTO>discardItemList=mainDashBoardService.findByItemSatus("폐기");
			System.out.println("MainDashBoardController discardItemList:"+discardItemList);
			model.addAttribute("discardItemList", discardItemList);
			
			//수주
			List<Sale>saleItemList=mainDashBoardService.findBySaleStatus("수주완료");
			System.out.println("MainDashBoardController saleItemList:"+saleItemList);
			model.addAttribute("saleItemList", saleItemList);
			
		
		return "main";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/login";
	}
	

	
	
	
	
	
}//
