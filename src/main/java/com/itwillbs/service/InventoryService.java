package com.itwillbs.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.repository.InventoryRepository;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    
	//재고 조회 쿼리 
	//품목코드, 품목이름, 품목유형(원재료, 가공품, 완제품)을 검색하는 기능 구현
	//셋 다 빈 문자열('')인 경우 해당조건을 무시하고 테이블 전체를 출력함
    public List<InventoryItemDTO> findItemWithInvendftory(InventoryItemDTO inventoryItemDTO) {
    	inventoryRepository.findAll();
    	
        return inventoryRepository.findItemWithInventory(
            inventoryItemDTO.getItemCode(),
            inventoryItemDTO.getItemName(),
            inventoryItemDTO.getItemType()
        );
    }
}
