package com.itwillbs.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingItemsDTO;

import com.itwillbs.domain.inventory.InvenSearchDTO;
import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.repository.IncomingItemsRepository;
import com.itwillbs.repository.IncomingRepository;
import com.itwillbs.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class InventoryService {
	private final InventoryRepository inventoryRepository;
	private final IncomingRepository incomingRepository;
	private final IncomingItemsRepository incomingItemsRepository;

	// 재고 전체 조회 (페이지네이션 지원)
	public Page<InventoryItemDTO> getInventoryItems(Pageable pageable) {
		log.info("getInventoryItems()");
		return inventoryRepository.getAllInventoryItems(pageable);
	}

	// 재고 부족 품목만 조회 (검색 조건 포함)
	public Page<InventoryItemDTO> findInventoryItemsByOutOfStock(String itemCodeOrName, String itemType,
			Pageable pageable) {
		log.info("findInventoryItemsByOutOfStock()");
		return inventoryRepository.findInventoryItemsByOutOfStock(itemCodeOrName, itemType, pageable);
	}

	// 재고 검색 (검색 조건과 페이지네이션)
	public Page<InventoryItemDTO> findInventoryItemsBySearch(String itemCodeOrName, String itemType, Pageable pageable) {
		log.info("findInventoryItems()");
		return inventoryRepository.findInventoryItems(itemCodeOrName, itemType, pageable);
	}

	// 입고 전체 조회
	public Page<IncomingDTO> getIncomingLists(Pageable pageable) {
		log.info("getIncomingLists()");

		// 입고 데이터 저장
		Page<IncomingDTO> incomingByPage = incomingRepository.getAllIncomingLists(pageable);

		// 각 입고데이터마다 품목의 이름과 갯수를 구하기 위한 반복문
		incomingByPage.forEach(dto -> {

			String incomingId = dto.getIncomingId();

			// incoming_items테이블에서 품목의 이름과 갯수를 구한다.
			List<IncomingItemsDTO> itemNames = incomingItemsRepository.findIncomingItemsListById(incomingId);

			// 품목중 첫번째 품목의 이름을 저장
			dto.setIncomingItemDisplay(itemNames.get(0).getItemName());

			// 품목 갯수 - 1을 저장
			dto.setOtherCount(itemNames.size() - 1);

		});

		return incomingByPage;
	}

	// 입고 검색 (검색 조건과 페이지네이션)
//	public Page<IncomingDTO> findIncomingBySearch(InvenSearchDTO searchDTO, Pageable pageable) {
//		log.info("findIncomingBySearch()");
//
//		Page<IncomingDTO> incomingByPage = incomingRepository.getAllIncomingLists(pageable);
//		
//		// 각 입고데이터마다 품목의 이름과 갯수를 구하기 위한 반복문
//		incomingByPage.forEach(dto -> {
//
//			String incomingId = dto.getIncomingId();
//
//			// incoming_items테이블에서 품목의 이름과 갯수를 구한다.
//			List<IncomingItemsDTO> itemNames = incomingItemsRepository.findIncomingItemsListById(incomingId);
//
//			// 품목중 첫번째 품목의 이름을 저장
//			dto.setIncomingItemDisplay(itemNames.get(0).getItemName());
//
//			// 품목 갯수 - 1을 저장
//			dto.setOtherCount(itemNames.size() - 1);
//
//		});
//		
//		return incomingByPage;
//	}

}
