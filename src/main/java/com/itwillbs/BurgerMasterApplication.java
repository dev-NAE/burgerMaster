package com.itwillbs;

import java.util.Date;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BurgerMasterApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.println("현재시각 : " + new Date());
	}
	public static void main(String[] args) {
		SpringApplication.run(BurgerMasterApplication.class, args);
	}

}
