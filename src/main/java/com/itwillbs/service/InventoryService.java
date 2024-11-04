package com.itwillbs.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@Service
@RequiredArgsConstructor
@Log
public class InventoryService {
    private final InventoryRepository inventoryRepository;
   

    
    
    //재고 전체 조회
	public List<InventoryItemDTO> getInventoryItems() {
		log.info("getInventoryitems()");
		
		return inventoryRepository.getAllInventoryItems();
//		return inventoryRepository.getInventoryItems();
	}
    
  

	public List<InventoryItemDTO> findInventoryItemsByOutOfStock() {
		
		
		return inventoryRepository.findInventoryItemsByOutOfStock();
	}


	
	
	public List<InventoryItemDTO> findInventoryItems(String itemCodeOrName, String itemType) {
		
		return inventoryRepository.findInventoryItems(itemCodeOrName, itemType);
	}






}
