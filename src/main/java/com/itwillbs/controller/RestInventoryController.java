package com.itwillbs.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;

@RestController
@RequestMapping("/restInven")
@Log
@RequiredArgsConstructor
public class RestInventoryController {
    
    private final InventoryService inventoryService;

    @GetMapping("/filterInventory")
    public ResponseEntity<List<InventoryItemDTO>> filterInventory(
            @RequestParam(name = "itemCodeOrName", required = false) String itemCodeOrName,
            @RequestParam(name = "itemType", required = false) String itemType,
            @RequestParam(name = "findOutOfStock", defaultValue = "false") boolean findOutOfStock){

        log.info("RestInventoryController filterInventory()");


        List<InventoryItemDTO> filteredItems;
        if (findOutOfStock == true) {
            filteredItems = inventoryService.findInventoryItemsByOutOfStock(itemCodeOrName, itemType);
        } else {
            filteredItems = inventoryService.findInventoryItems(itemCodeOrName, itemType);
        }


        return ResponseEntity.ok(filteredItems);
    }
    
    
}
