package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	@GetMapping("/exampleFile")
	public String exampleFile() {
		return "exampleFile";
	}
	
	
}
