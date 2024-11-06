package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.entity.QualityOrder;
import com.itwillbs.service.QualityOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class QualityController {

	private final QualityOrderService qualityOrderService;
	
	
	//추후에 지워야함
	@GetMapping("/formSample")
	public String formSample() {
		log.info("QualityController formSample().....");
		return "/quality/formSample";
	} 
	
	@GetMapping("/quality/qualityList")
	public String qualityList() {
		log.info("QualityController qualityList().....");
		return "/quality/qualityList";
	}

	@GetMapping("/quality/quality_shipment")
	public String quality_shipment() {
		log.info("QualityController quality_shipment().....");
		return "/quality/quality_shipment";
	}

	@GetMapping("/quality/quality_sale")
	public String quality_sale() {
		log.info("QualityController quality_sale().....");
		return "/quality/quality_sale";
	}

	@GetMapping("/quality/quality_order")
	public String quality_order(Model model) {
		log.info("QualityController quality_order()....."); 
		
		List<QualityOrder> list = qualityOrderService.getDashBaord();
		List<String> itemList;
		
		for(int i = 0; i < list.size(); i++) {
			
			//qualityOrderService.getItemName(list.get(i).getItem_code());
			
			
		}
		
		model.addAttribute("qualityList", qualityOrderService.getDashBaord());
		
		return "/quality/quality_order";
	}
	
	@GetMapping("/defective/defectiveList")
	public String defective() {
		log.info("QualityController defective().....");
		return "/quality/defectiveList";
	}
	

}
