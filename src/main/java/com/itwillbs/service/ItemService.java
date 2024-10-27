package com.itwillbs.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.masterdata.ItemSearchDTO;
import com.itwillbs.entity.Item;
import com.itwillbs.repository.ItemRepository;

@Service
public class ItemService {
	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public Optional<Item> findItemByCode(String code) {
		return itemRepository.findById(code);
	}

	public Item saveItem(Item item) {
		return itemRepository.save(item);
	}

	public Item updateItem(Item item) {
		return itemRepository.save(item);
	}

	public void deleteItem(String itemCode) {
		itemRepository.deleteById(itemCode);
	}

	public String generateNextCode(String itemType) {
		String maxCode = itemRepository.findMaxItemCodeByItemType(itemType);
		if(maxCode == null) {
			return itemType + "001";
		}
		int nextNumber = Integer.parseInt(maxCode.substring(2)) + 1;
		return String.format("%s%03d", itemType, nextNumber);
	}

	public Page<Item> searchItems(ItemSearchDTO searchDto, PageRequest of) {
		return itemRepository.findAll(of);
	}
	
	
	
	
}