package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.service.MFService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/mf")
public class MFController {
	
	private final MFService mfService;
	
	@GetMapping("/orders")
	public String orders() {
		
		return "/manufacture/orders";
	}
	
	@GetMapping("/bom")
	public String bom() {
		
		return "/manufacture/BOM";
	}
	
	@GetMapping("/insert")
	public String insert() {
		
		return "/manufacture/orderInsert";
	}
}
