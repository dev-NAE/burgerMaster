package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class MFController {
	
	@GetMapping("/MFOrders")
	public String orders() {
		
		return "/manufacture/orders";
	}
	
	@GetMapping("/MFBom")
	public String bom() {
		
		return "/manufacture/BOM";
	}
}
