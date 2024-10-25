package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.java.Log;

@Controller
@Log
public class MainDashBoardController {

	@GetMapping("/main")
	public String main() {
		log.info("MainDashBoardController main");
		
		
		
		return "main";
	}
	
	
	
}//
