package com.itwillbs.controller;




import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.inventory.IncomingDTO;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.service.InventoryService;

/**
 * 입고, 출고, 재고 관리하는 컨트롤러
 */
@Controller
@RequestMapping("/inven")
@Slf4j
@RequiredArgsConstructor
public class InventoryController {

	// 공통된 View 경로를 상수로 정의
	private static final String VIEW_PATH = "inventory_management/";

	private final InventoryService inventoryService;

//    재고 조회
	@GetMapping("/inventoryList")
	public String inventoryList(Model model, @PageableDefault(size = 8) Pageable pageable) {
		log.info("InventoryController inventoryList()");

		// 페이징 처리하여 재고 중 10개의 데이터만 저장
		Page<InventoryItemDTO> inventoryItemByPage = inventoryService.getInventoryItems(pageable);

		// 모델에 조회된 재고 데이터를 저장
		model.addAttribute("inventoryItemDTOs", inventoryItemByPage.getContent());

		// 검색 조건의 기본값 설정
		model.addAttribute("itemCodeOrName", "");
		model.addAttribute("itemType", "");
		model.addAttribute("findOutOfStock", "N");

		// 페이징 처리하고 model에 저장
		applyPagination(inventoryItemByPage, model);

		return VIEW_PATH + "inventory_list";
	}

	// 재고 조회 검색
	@GetMapping("/inventoryListSearch")
	public String inventoryListSearch(Model model,
			@RequestParam(name = "itemCodeOrName", defaultValue = "") String itemCodeOrName,
			@RequestParam(name = "itemType", defaultValue = "") String itemType,
			@RequestParam(name = "findOutOfStock", defaultValue = "N") String findOutOfStock,
			@PageableDefault(size = 8) Pageable pageable) {

		log.info("InventoryController inventoryListSearch()");

		Page<InventoryItemDTO> inventoryItemByPage;

		boolean isFindOutOfStock = "Y".equals(findOutOfStock);

		if (isFindOutOfStock == true) {
			inventoryItemByPage = inventoryService.findInventoryItemsByOutOfStock(itemCodeOrName, itemType, pageable);
		} else {
			inventoryItemByPage = inventoryService.findInventoryItemsBySearch(itemCodeOrName, itemType, pageable);
		}

		model.addAttribute("inventoryItemDTOs", inventoryItemByPage.getContent());
		model.addAttribute("itemCodeOrName", itemCodeOrName);
		model.addAttribute("itemType", itemType);
		model.addAttribute("findOutOfStock", findOutOfStock);

		// 페이징 처리하고 model에 저장
		applyPagination(inventoryItemByPage, model);

		return VIEW_PATH + "inventory_list";
	}

	// 입고 등록
	@GetMapping("/incomingInsert")
	public String incomingInsert() {
		log.info("InventroyController incomingInsert()");

		return VIEW_PATH + "incoming_insert";
	}

	// 입고 조회
	@GetMapping("/incomingList")
	public String incomingList(Model model, @PageableDefault(size = 8) Pageable pageable) {
		log.info("InventroyController incomingList()");

		// 입고된 리스트 전체 조회
		Page<IncomingDTO> incomingByPage = inventoryService.getIncomingLists(pageable);
//		log.info("Incoming DTOs: {}", incomingByPage.getContent());


		// model에 입고테이블에 출력할 데이터 저장
		model.addAttribute("incomingDTOs", incomingByPage);

		// 검색 조건의 기본값 설정
		model.addAttribute("itemCodeOrName", "");
		model.addAttribute("reasonOfIncoming", "");
		model.addAttribute("incomingStartDate_start", null);
		model.addAttribute("incomingStartDate_end", null);
		model.addAttribute("incomingId", "");
		model.addAttribute("prodOrQualId", "");
		model.addAttribute("status", "");
		model.addAttribute("managerCodeOrName", "");
		
		// 페이징 처리하고 model에 저장
		applyPagination(incomingByPage, model);

		return VIEW_PATH + "incoming_list";
	}

	@GetMapping("/incomingListSearch")
	public String incomingListSearch(Model model, @PageableDefault(size = 8) Pageable pageable,
	        @RequestParam(name = "itemCodeOrName", defaultValue = "") String itemCodeOrName,
	        @RequestParam(name = "reasonOfIncoming", defaultValue = "") String reasonOfIncoming,
            @RequestParam(name = "incomingStartDate_start", defaultValue = "") String incomingStartDate_startStr,
            @RequestParam(name = "incomingStartDate_end", defaultValue = "") String incomingStartDate_endStr,
	        @RequestParam(name = "incomingId", defaultValue = "") String incomingId,
	        @RequestParam(name = "prodOrQualId", defaultValue = "") String prodOrQualId,
	        @RequestParam(name = "status", defaultValue = "") String status,
	        @RequestParam(name = "managerCodeOrName", defaultValue = "") String managerCodeOrName) {

		log.info("InventoryController incomingListSearch()");
		log.info("넘어온것 :");
		log.info("itemCodeOrName = " + itemCodeOrName);
		log.info("reasonOfIncoming = " + reasonOfIncoming);

		log.info("incomingId = " + incomingId);
		log.info("prodOrQualId = " + prodOrQualId);
		log.info("status = " + status);
		log.info("managerCodeOrName = " + managerCodeOrName);
		
	    Timestamp incomingStartDate_start = null; 
	    Timestamp incomingStartDate_end = null;
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    //문자열로 가져온 등록일검색 날짜는 DB에서 조회할때 TimeStamp 타입으로 변환해야 한다
	    try {
	        // 시작 날짜 처리
	        if (incomingStartDate_startStr != null && !incomingStartDate_startStr.isEmpty()) {
	            String startDateTimeStr = incomingStartDate_startStr + " 00:00:00";
	            incomingStartDate_start = Timestamp.valueOf(startDateTimeStr);
	        }

	        // 종료 날짜 처리
	        if (incomingStartDate_endStr != null && !incomingStartDate_endStr.isEmpty()) {
	            String endDateTimeStr = incomingStartDate_endStr + " 23:59:59";
	            incomingStartDate_end = Timestamp.valueOf(endDateTimeStr);
	        }
	    } catch (IllegalArgumentException e) {
	        log.error("날짜 변환 오류: " + e.getMessage());
	        // 예외 처리: 사용자에게 에러 메시지 전달하거나 기본값 설정
	        // 예를 들어, 에러 메시지를 모델에 추가
	        model.addAttribute("errorMessage", "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식을 사용하세요.");
	        // 검색 결과를 빈 상태로 반환하거나, 전체 데이터를 보여줄 수 있습니다.
	        // 여기서는 빈 결과를 반환합니다.
	        model.addAttribute("incomingDTOs", new ArrayList<>());
	        return VIEW_PATH + "incoming_list";
	    }
	
	    log.info("sdf" + incomingStartDate_start);
		
		
		Page<IncomingDTO> incomingByPage = inventoryService.findIncomingBySearch(
																	            itemCodeOrName,
																	            reasonOfIncoming,
																	            incomingStartDate_start,
																	            incomingStartDate_end,
																	            incomingId,
																	            prodOrQualId,
																	            status,
																	            managerCodeOrName,
																	            pageable);

	    model.addAttribute("incomingDTOs", incomingByPage);


		
		// 검색 조건의 기본값 설정
	    model.addAttribute("itemCodeOrName", itemCodeOrName);
	    model.addAttribute("reasonOfIncoming", reasonOfIncoming);
	    model.addAttribute("incomingStartDate_start", incomingStartDate_startStr);
	    model.addAttribute("incomingStartDate_end", incomingStartDate_endStr);
	    model.addAttribute("incomingId", incomingId);
	    model.addAttribute("prodOrQualId", prodOrQualId);
	    model.addAttribute("status", status);
	    model.addAttribute("managerCodeOrName", managerCodeOrName);
		
		
		// 페이징 처리하고 model에 저장
		applyPagination(incomingByPage, model);

		return VIEW_PATH + "incoming_list";
	}

	// 출고 등록
	@GetMapping("/outgoingInsert")
	public String outgoingInsert() {
		log.info("InventroyController outgoingInsert()");

		return VIEW_PATH + "outgoing_insert";
	}

	// 출고 조회
	@GetMapping("/outgoingList")
	public String outgoingList(Model model, @PageableDefault(size = 10) Pageable pageable) {
		log.info("InventroyController outgoingList()");

		return VIEW_PATH + "outgoing_list";
	}

	// 페이지네이션 구현 메서드
	private void applyPagination(Page<?> page, Model model) {


		// 불러온 데이터가 없으면 예외처리
		if (page == null || !page.hasContent()) {

			model.addAttribute("currentPage", 1);
			model.addAttribute("totalPages", 1);
			model.addAttribute("startPage", 1);
			model.addAttribute("endPage", 1);
			return;
		}

		int currentPage = page.getNumber() + 1; // 현재 페이지 (1부터 시작)
		int totalPages = page.getTotalPages(); // 총 페이지 수
		int startPage, endPage;

		// 현재페이지가 1일때, 끝 페이지 일때, 중간에 있을때로 구분
		// 만약 총 페이지 수가 8이고 현재페이지가 1이면 1,2로 표시하고
		// 5이면 4,5,6으로 표시하고
		// 8이면 7,8로 표시
		if (currentPage == 1) {
			startPage = currentPage;
			endPage = Math.min(totalPages, currentPage + 1);
		} else if (currentPage == totalPages) {
			startPage = Math.max(1, currentPage - 1);
			endPage = currentPage;
		} else {
			startPage = currentPage - 1;
			endPage = currentPage + 1;
		}
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		return;
	}
	

}
