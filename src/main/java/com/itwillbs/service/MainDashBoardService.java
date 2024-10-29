package com.itwillbs.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.test.ItemDashDTO;
import com.itwillbs.entity.Item;
import com.itwillbs.repository.MainDashBoardItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@Service
@Log
@RequiredArgsConstructor
public class MainDashBoardService {
	
	private final MainDashBoardItemRepository mainDashBoardRepository;

//	public List<Object[]> getItemListByType(String itemType) {
//		log.info("getItemListByType"+itemType);
//		System.out.println("MainDashBoardService itemType:"+itemType);
//		
//		 return mainDashBoardRepository.findByItemType(itemType);
//		 
//	}
	
//	public List<Object[]> findAndPrintRMTypeItems (String itemType) {
//        List<Object[]> rmItemList = mainDashBoardRepository.findByItemType(itemType);
//
//        for (Object[] row : rmItemList) {
//        	BigDecimal quantity = (BigDecimal) row[0];  // 첫 번째 요소는 quantity
//            String itemName = (String) row[1];          // 두 번째 요소는 itemName
//
//            System.out.println("Quantity: " + quantity + ", Item Name: " + itemName);
//            System.out.println("rmItemList="+rmItemList);
//        }
//		return rmItemList;
//    }
	
	 public List<ItemDashDTO> getItemListByType(String itemType) {
	        List<ItemDashDTO> ItemList = mainDashBoardRepository.findByItemType(itemType);
	        
	        // 데이터 출력
	        for (ItemDashDTO item : ItemList) {
	            System.out.println("Quantity: " + item.getQuantity() + ", Item Name: " + item.getItemName());
	        }
	        
	     
	        return ItemList; // DTO 리스트 반환
	    }
	
	



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//
