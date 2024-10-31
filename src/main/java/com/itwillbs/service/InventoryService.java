package com.itwillbs.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


/**
 *  전체 조회 메서드명: get...() <br>
 *  검색 조회 메서드명: find...() <br>
 */
@Service
@RequiredArgsConstructor
@Log
public class InventoryService {
    private final InventoryRepository inventoryRepository;
   

    
    
    //재고 전체 조회
	public List<InventoryItemDTO> getInventoryItems() {
		log.info("getInventoryitems()");
		
		return inventoryRepository.getItemInventory();
	}
    
  
    
	//재고 검색
	//품목코드, 품목명, 품목유형이 빈문자('')라도 검색가능
    public List<InventoryItemDTO> findInventoryItems(InventoryItemDTO inventoryItemDTO) {
    	log.info("findInventoryitems()");
    	
        return inventoryRepository.findItemWithInventory(
            inventoryItemDTO.getItemCode(),
            inventoryItemDTO.getItemName(),
            inventoryItemDTO.getItemType()
        );
    }



}
