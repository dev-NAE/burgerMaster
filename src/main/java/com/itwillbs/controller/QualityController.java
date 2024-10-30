package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class QualityController {

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
	public String quality_order() {
		log.info("QualityController quality_order().....");
		return "/quality/quality_order";
	}
	
	@GetMapping("/defective/defectiveList")
	public String defective() {
		log.info("QualityController defective().....");
		return "/quality/defectiveList";
	}
	

}
