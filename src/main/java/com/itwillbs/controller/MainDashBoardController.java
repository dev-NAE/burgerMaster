package com.itwillbs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
		
		List<Item>ItemList=mainDashBoardService.getItemList();
		model.addAttribute("ItemList", ItemList);
		System.out.println("ItemList:"+ItemList);
		
		return "main";
	}
	
	
	
	
	
}//
