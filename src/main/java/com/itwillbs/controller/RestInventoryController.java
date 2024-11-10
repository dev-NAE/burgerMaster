package com.itwillbs.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.entity.Incoming;
import com.itwillbs.entity.IncomingItems;
import com.itwillbs.repository.IncomingRepository;
import com.itwillbs.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restInven")
@Log
@RequiredArgsConstructor
public class RestInventoryController {
    
    private final InventoryService inventoryService;

//    @GetMapping("/filterInventory")
//    public ResponseEntity<List<InventoryItemDTO>> filterInventory(
//            @RequestParam(name = "itemCodeOrName", required = false) String itemCodeOrName,
//            @RequestParam(name = "itemType", required = false) String itemType,
//            @RequestParam(name = "findOutOfStock", defaultValue = "false") boolean findOutOfStock){
//
//        log.info("RestInventoryController filterInventory()");
//
//
//        List<InventoryItemDTO> filteredItems;
//        if (findOutOfStock == true) {
//            filteredItems = inventoryService.findInventoryItemsByOutOfStock(itemCodeOrName, itemType);
//        } else {
//            filteredItems = inventoryService.findInventoryItems(itemCodeOrName, itemType);
//        }
//
//
//        return ResponseEntity.ok(filteredItems);
//    }
//    
    
    @GetMapping("/incomingDetail")
    public ResponseEntity<List<IncomingItemsDTO>> getIncomingDetail(@RequestParam(name = "incomingId") String incomingId) {
    	log.info("RestInventoryController.getIncomingDetail()");
        // 입고 품목 리스트 가져오기
        List<IncomingItemsDTO> incomingItemsDTO = inventoryService.getIncomingItems(incomingId);
        
        // itemType 매핑
        incomingItemsDTO.forEach(item -> {
            switch(item.getItemType()) {
                case "FP":
                    item.setItemType("완제품");
                    break;
                case "RM":
                    item.setItemType("원재료");
                    break;
                case "PP":
                    item.setItemType("가공품");
                    break;
                default:
                    item.setItemType("알 수 없음");
            }
        });
        
        // 입고 품목이 존재하면 반환
        if (!incomingItemsDTO.isEmpty()) {
            return ResponseEntity.ok(incomingItemsDTO);
        } else {
        	log.info("입고 품목 정보가 없습니다. incomingId: " + incomingId);
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
