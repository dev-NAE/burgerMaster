package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.domain.manufacture.MFBomDTO;
import com.itwillbs.domain.manufacture.MFOrderDTO;
import com.itwillbs.domain.manufacture.MFRmListDTO;
import com.itwillbs.entity.Item;
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
		
		List<MFOrderDTO> orderList = mfService.getOrderList();
		
		model.addAttribute("orderList", orderList);
		
		return "/manufacture/orders";
	}
	
	@GetMapping("/bom")
	public String bom(Model model) {
		log.info("MFController bom()");
		
		List<MFBomDTO> bomList = new ArrayList<>();
		
		List<Item> ppList = mfService.getPPList();
		
		for(int i = 0; i<ppList.size(); i++) {
			MFBomDTO bom = new MFBomDTO();
			
			bom.setItemCode(ppList.get(i).getItemCode());
			bom.setItemName(ppList.get(i).getItemName());
			
			bomList.add(bom);
		}
		
		bomList = mfService.getRmList(bomList);
		
		log.info(bomList.toString());
		
		model.addAttribute("bomList", bomList);
		
		return "/manufacture/BOM";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		log.info("MFController insert()");
		
		List<Item> ppList = mfService.getPPList();
		
		model.addAttribute("ppList", ppList);
		
		return "/manufacture/orderInsert";
	}
	
	@ResponseBody
	@GetMapping("/getRM")
	public ResponseEntity<List<MFRmListDTO>> getRM(@RequestParam(name = "itemName") String itemName){
		log.info("MFController getRM()");
		
		List<MFRmListDTO> rmList = mfService.getRM(itemName);
		log.info(rmList.toString());
		
		return ResponseEntity.ok(rmList);
		
	}
	
	
}
