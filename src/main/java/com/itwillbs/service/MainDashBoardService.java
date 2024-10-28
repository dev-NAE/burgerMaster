package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.test.ITEM;
import com.itwillbs.repository.MainDashBoardItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@Service
@Log
@RequiredArgsConstructor
public class MainDashBoardService {
	
	private final MainDashBoardItemRepository mainDashBoardRepository;

	public List<ITEM> getItemListByType(String itemType) {
		log.info("getItemListByType"+itemType);
		System.out.println("MainDashBoardService itemType:"+itemType);
		
		 return mainDashBoardRepository.findByItemType(itemType);
		 
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//
