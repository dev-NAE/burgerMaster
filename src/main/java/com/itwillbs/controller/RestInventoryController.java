package com.itwillbs.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingInsertDTO;
import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.domain.inventory.InvenResponseMessage;
import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.entity.Incoming;
import com.itwillbs.entity.IncomingItems;
import com.itwillbs.repository.IncomingRepository;
import com.itwillbs.service.InventoryService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restInven")
@Slf4j
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
	public ResponseEntity<List<IncomingItemsDTO>> getIncomingDetail(
			@RequestParam(name = "incomingId") String incomingId) {
		log.info("RestInventoryController.getIncomingDetail()");
		// 입고 품목 리스트 가져오기
		List<IncomingItemsDTO> incomingItemsDTO = inventoryService.getIncomingItems(incomingId);

		// itemType 매핑
		incomingItemsDTO.forEach(item -> {
			switch (item.getItemType()) {
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

	@PostMapping("/updateIncomingStatus")
	public ResponseEntity<InvenResponseMessage> updateIncomingStatus(@RequestParam("incomingId") String incomingId) {
		log.info("RestInventoryController.updateIncomingStatus()");

		// 업데이트 실행, 성공or에러시 적절한 메세지를 프론트로 넘김
		try {
			inventoryService.updateIncomingStatus(incomingId);
			InvenResponseMessage response = new InvenResponseMessage(true, "입고 완료되었습니다.");
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			InvenResponseMessage response = new InvenResponseMessage(false, "해당 입고 ID의 입고 상태를 업데이트할 수 없습니다.");
			// 400에러
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} catch (Exception e) {
			InvenResponseMessage response = new InvenResponseMessage(false, "서버 오류가 발생했습니다.");
			// 500에러
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 입고 등록할 목록들 조회
	@GetMapping("/incomingInsertList")
	public ResponseEntity<List<IncomingInsertDTO>> incomingInsertList() {
		log.info("RestInventoryController.incomingInsertList()");

		List<IncomingInsertDTO> incomingInsertDTO = new ArrayList<>();
//    	List<IncomingInsertDTO> incomingInsertDTO = inventoryService.findIncomingInsertList();

		// 입고 등록할 품목이 존재하면 반환
		if (!incomingInsertDTO.isEmpty()) {
			return ResponseEntity.ok(incomingInsertDTO);
		} else {
			log.info("입고 등록할 품목이 존재하지 않습니다.");
			return ResponseEntity.ok(incomingInsertDTO);
		}
	}

	@GetMapping("/findManagerList")
	public ResponseEntity<List<IncomingInsertDTO>> findManagerList() {
		log.info("RestInventoryController.findManagerList()");

		List<IncomingInsertDTO> incomingInsertDTO = new ArrayList<>();
//    	List<IncomingInsertDTO> incomingInsertDTO = inventoryService.findIncomingInsertList();

		// 입고 등록할 품목이 존재하면 반환
		if (!incomingInsertDTO.isEmpty()) {
			return ResponseEntity.ok(incomingInsertDTO);
		} else {
			log.info("관리자 정보를 찾을 수 없습니다.");
			return ResponseEntity.ok(incomingInsertDTO);
		}
	}

}
