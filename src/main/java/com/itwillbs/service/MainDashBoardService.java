package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.dashboard.DefectiveDTO;
import com.itwillbs.domain.dashboard.IncomingItemDTO;
import com.itwillbs.domain.dashboard.InventoryItemDTO;
import com.itwillbs.domain.dashboard.ItemDashDTO;
import com.itwillbs.entity.dashboard.SaleDash;
import com.itwillbs.repository.MainDashBoardItemRepository;
import com.itwillbs.repository.MainDashBoardSaleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@Service
@Log
@RequiredArgsConstructor
public class MainDashBoardService {
	
	private final MainDashBoardItemRepository mainDashBoardRepository;
	private final MainDashBoardSaleRepository dashBoardSaleRepository;
	

	 public List<DefectiveDTO> findByStatus(String status) {
	        List<DefectiveDTO> ItemList = mainDashBoardRepository.findByStatus(status);
	        
	        for (DefectiveDTO item : ItemList) {
	            System.out.println("Quantity: " + item.getQuantity() + ", Item Name: " + item.getItemName());
	        }
	        
	     
	        return ItemList; 
	    }
	
	 public List<SaleDash> findBySaleStatus(String status) {
	        List<SaleDash> ItemList = dashBoardSaleRepository.findByStatus(status);
	        
	        for (SaleDash item : ItemList) {
	            System.out.println("totalPrice: " + item.getTotalPrice() + ", dueDate: " + item.getDueDate());
	        }
	        
	     
	        return ItemList; 
	    }

	 
	public List<IncomingItemDTO> getItemIncomming(String itemType) {
		
		List<IncomingItemDTO> ItemList = mainDashBoardRepository.findByItemType(itemType);

	        
	        for (IncomingItemDTO item : ItemList) {
	            System.out.println("Quantity: " + item.getQuantity() + ", Item Name: " + item.getItemName());
	        }
	        
	     
	        return ItemList; 
	    }

	public List<InventoryItemDTO> getItemInventory(String itemType) {
		List<InventoryItemDTO> ItemList = mainDashBoardRepository.findByInventoryItemType(itemType);

        
        for (InventoryItemDTO item : ItemList) {
            System.out.println("Quantity: " + item.getQuantity() + ", Item Name: " + item.getItemName());
        }
        
     
		return ItemList;
	}


	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//
