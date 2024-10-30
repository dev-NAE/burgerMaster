package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.service.MFService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class MFController {
	
	private final MFService mfService;
	
	@GetMapping("/MFOrders")
	public String orders() {
		
		return "/manufacture/orders";
	}
	
	@GetMapping("/MFBom")
	public String bom() {
		
		return "/manufacture/BOM";
	}
}
