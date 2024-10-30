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
		validateItem(item);
		return itemRepository.save(item);
	}

	@Transactional
	public Item updateItem(Item item) {
		validateItem(item);
		return itemRepository.save(item);
	}

	private void validateItem(Item item) {
//		if (!item.getItemCode().matches("^(RM|PP|FP)\\d{3}$")) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품목코드 형식이 올바르지 않습니다. (RM/PP/FP + 3자리 숫자)");
//		}
//		if (itemRepository.existsByItemCode(item.getItemCode())) {
//			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 품목코드입니다.");
//		}
//		String codePrefix = item.getItemCode().substring(0, 2);
//		if (!codePrefix.equals(item.getItemType())) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "품목 유형과 코드 prefix가 일치하지 않습니다.");
//		}
//		if (item.getUseYN() != 'Y' && item.getUseYN() != 'N') {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용 여부는 Y 또는 N이어야 합니다.");
//		}
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