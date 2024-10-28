package com.itwillbs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.entity.test.ITEM;
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
			List<ITEM>rmItemList=mainDashBoardService.getItemListByType("RM");
			System.out.println("rmItemList:"+rmItemList);
			model.addAttribute("rmItemList", rmItemList);
			
			//가공품
			List<ITEM>ppItemList=mainDashBoardService.getItemListByType("PP");
			System.out.println("ppItemList:"+ppItemList);
			model.addAttribute("ppItemList", ppItemList);
			
			//완재품
			List<ITEM>fpItemList=mainDashBoardService.getItemListByType("FP");
			System.out.println("fpItemList:"+fpItemList);
			model.addAttribute("fpItemList", fpItemList);
			
			//불량률
			
			//반품률
		
		return "main";
	}
	
	
	
	
	
}//
