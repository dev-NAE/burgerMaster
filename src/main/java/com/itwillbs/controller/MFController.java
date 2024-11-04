package com.itwillbs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.entity.MFOrder;
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
	public String orders(Model model) {
		log.info("MFController order()");
		
		List<MFOrder> orderList = mfService.getOrderList();
		
		model.addAttribute("orderList", orderList);
		
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
