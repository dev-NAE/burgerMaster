package com.itwillbs.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.domain.test.ItemDashDTO;
import com.itwillbs.entity.Item;
import com.itwillbs.service.MainDashBoardService;

import ch.qos.logback.core.model.Model;
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
		
			//원재료
			List<ItemDashDTO>rmItemList=mainDashBoardService.getItemListByType("RM");
			
			
			System.out.println("MainDashBoardController rmItemList:"+rmItemList);
			model.addAttribute("rmItemList", rmItemList);
			
			//가공품
			List<ItemDashDTO>ppItemList=mainDashBoardService.getItemListByType("PP");
			System.out.println("MainDashBoardController ppItemList:"+ppItemList);
			model.addAttribute("ppItemList", ppItemList);
			
			//완재품
			List<ItemDashDTO>fpItemList=mainDashBoardService.getItemListByType("FP");
			System.out.println("MainDashBoardController fpItemList:"+fpItemList);
			model.addAttribute("fpItemList", fpItemList);
			
			//불량률
			
			//반품률
		
		return "main";
	}
	
	
	
	
	
}//
