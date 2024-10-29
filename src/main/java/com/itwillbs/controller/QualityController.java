package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class QualityController {

	@GetMapping("/qualityList")
	public String qualityList() {
		log.info("QualityController qualityList().....");
		return "/quality/qualityList";
	}






}
