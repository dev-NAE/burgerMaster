package com.itwillbs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.itwillbs.domain.masterdata.ItemDTO;
import com.itwillbs.domain.masterdata.ItemSearchDTO;
import com.itwillbs.entity.InventoryItem;
import com.itwillbs.entity.Item;
import com.itwillbs.repository.InventoryRepository;
import com.itwillbs.repository.ItemRepository;

@Service
@Transactional(readOnly = true)
public class ItemService {
	private final ItemRepository itemRepository;
	private final InventoryRepository inventoryRepository;
	

	
 
    public ItemService(ItemRepository itemRepository, InventoryRepository inventoryRepository) {
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
    }
	
	
	
	public Page<Item> searchItems(ItemSearchDTO searchDTO, Pageable pageable) {
		return itemRepository.findBySearchConditions(searchDTO.getItemName(), searchDTO.getItemType(),
				searchDTO.getIncludeUnused(), pageable);
	}
	
	public Optional<Item> findItemByCode(String itemCode) {
		return itemRepository.findById(itemCode);
	}

	@Transactional
	public Item saveItem(Item item) {
		validateItemCode(item);
		validateDuplicate(item);
		
	    // Item을 먼저 저장
	    Item savedItem = itemRepository.save(item);
	    
	    // InventoryItem 객체 생성 후 item_code 설정 및 기본값 설정
	    InventoryItem inventoryItem = new InventoryItem();
	    inventoryItem.setItemCode(savedItem.getItemCode());
	    inventoryItem.setQuantity(0);           // 기본값 설정
	    inventoryItem.setMinReqQuantity(-1);    // 기본값 설정
	    
	    // InventoryItem 저장
	    inventoryRepository.save(inventoryItem);
	    
	    return savedItem;
	}

	@Transactional
	public Item updateItem(Item item) {
		validateItemCode(item);
		return itemRepository.save(item);
	}

	private void validateItemCode(Item item) {
		if (!item.getItemCode().startsWith(item.getItemType())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품목코드와 품목유형이 일치하지 않습니다.");
		}
	}

	private void validateDuplicate(Item item) {
		if (itemRepository.existsById(item.getItemCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 품목코드입니다.");
		}
	}

	@Transactional
	public void deleteItem(String itemCode) {
		itemRepository.deleteById(itemCode);
	}

	public String generateNextCode(String itemType) {
		String maxCode = itemRepository.findMaxItemCodeByItemType(itemType);
		if (maxCode == null) {
			return itemType + "001";
		}
		int nextNumber = Integer.parseInt(maxCode.substring(2)) + 1;
		return String.format("%s%03d", itemType, nextNumber);
	}
    
    public List<ItemDTO> searchItemsForModal(String itemType, String itemName, String useYN) {
		return itemRepository.findItemsForModal(itemType, itemName, useYN);
	}
	
}