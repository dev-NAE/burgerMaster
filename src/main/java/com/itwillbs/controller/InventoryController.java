package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {

	@GetMapping("/inventoryList")
	public String inventoryList() {
		
		
		return "inventory/inventoryList";
	}
	
}
