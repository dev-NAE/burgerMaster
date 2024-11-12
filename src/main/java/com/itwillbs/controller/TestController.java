package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

	@GetMapping("/exampleFile")
	public String exampleFile() {
		return "exampleFile";
	}
	


	@GetMapping("/sample")
	public String sample() {
		return "sample";
	}
	
	
	
	
}
