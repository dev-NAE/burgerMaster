package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.Item;
import com.itwillbs.repository.MainDashBoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@Log
@RequiredArgsConstructor
public class MainDashBoardService {
	
	private final MainDashBoardRepository mainDashBoardRepository;

	public List<Item> getItemList() {
		log.info("MainDashBoardService getItemList");
		
		mainDashBoardRepository.findAll();
		return null;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//
