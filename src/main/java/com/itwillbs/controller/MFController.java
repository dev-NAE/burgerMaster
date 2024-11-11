package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	public String bom(Model model,
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size) {
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
		
//		리스트를 페이지 객체로
		PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("itemCode").descending());
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()),bomList.size());
		Page<MFBomDTO> bomPage = new PageImpl<>(bomList.subList(start, end), pageRequest, bomList.size());
		
		model.addAttribute("bomList", bomPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("totalPages", bomPage.getTotalPages());
		
		int pageBlock = 3;
		int startPage = (page-1)/pageBlock*pageBlock+1;
		int endPage=startPage + pageBlock - 1;
		if(endPage > bomPage.getTotalPages()) {
			endPage = bomPage.getTotalPages();
		}
		
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
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
