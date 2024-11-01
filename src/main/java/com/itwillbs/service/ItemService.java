package com.itwillbs.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

	@Transactional
	public Item saveItem(Item item) {
		validateItemCode(item);
		validateDuplicate(item);
		validateItemName(item);
		return itemRepository.save(item);
	}

	@Transactional
	public Item updateItem(Item item) {
		validateItemCode(item);
		validateItemName(item);
		return itemRepository.save(item);
	}

	// 공통 검증 메서드들
	private void validateItemCode(Item item) {
		if (!item.getItemCode().matches("^(RM|PP|FP)\\d{3}$")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품목코드는 RM/PP/FP로 시작하고 3자리 숫자여야 합니다.");
		}
		if (!item.getItemCode().startsWith(item.getItemType())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품목코드와 품목유형이 일치하지 않습니다.");
		}
	}

	private void validateDuplicate(Item item) {
		if (itemRepository.existsByItemCode(item.getItemCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 품목코드입니다.");
		}
	}

	private void validateItemName(Item item) {
		if (!item.getItemName().matches("^[가-힣A-Za-z0-9\\s\\-\\_]+$")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품목명에 허용되지 않는 문자가 포함되어 있습니다.");
		}
	}

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

	public Page<Item> searchItems(ItemSearchDTO searchDto, Pageable pageable) {
		return itemRepository.findBySearchConditions(searchDto.getItemName(), searchDto.getItemType(),
				searchDto.getIncludeUnused(), pageable);
	}
}