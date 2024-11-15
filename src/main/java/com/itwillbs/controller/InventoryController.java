package com.itwillbs.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwillbs.config.security.util.SecurityUtil;
import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.domain.inventory.OutgoingDTO;
import com.itwillbs.service.InventoryService;

import jakarta.persistence.EntityNotFoundException;

/**
 * 출고, 출고, 재고 관리하는 컨트롤러
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
	public String inventoryList(Model model, @PageableDefault(size = 10) Pageable pageable) {
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
			@PageableDefault(size = 10) Pageable pageable) {

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
		log.info("InventroyController incomingInsertget()");
		

		
		
		return VIEW_PATH + "incoming_insert";
	}

	// 입고 등록post
	@PostMapping("/incomingInsert")
	public String incomingInsertPost(@RequestParam(name = "incomingInsertCode") String incomingInsertCode,
							            @RequestParam(name = "reasonOfIncoming") String reasonOfIncoming,
							            @RequestParam(name = "managerId") String managerId) {
		log.info("InventroyController incomingInsertPost()");
		log.info("incomingInsertCode = " + incomingInsertCode);
	    log.info("reasonOfIncoming = " + reasonOfIncoming);
		log.info("managerId = " + managerId);
		
		//입고등록하기
		
		//11월 13일 변경!! 발주완료되었지만 입고 등록되지 않은 발주번호를 가져와서 입고 테이블에 등록함 + 입고 품목테이블에도 등록함
		inventoryService.insertIncoming(incomingInsertCode, reasonOfIncoming, managerId);
		
		
		
		
		
		return "redirect:/inven/incomingInsert";
	}
	
	
	// 입고 조회
	@GetMapping("/incomingList")
	public String incomingList(Model model, @PageableDefault(size = 8) Pageable pageable) {
		log.info("InventroyController incomingList()");

		// 입고된 리스트 1페이지 조회
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
		model.addAttribute("prodOrOrderId", "");
		model.addAttribute("status", "");
		model.addAttribute("managerCodeOrName", "");
		
        // 현재 사용자의 권한 정보 추가
        List<String> userRoles = SecurityUtil.getUserAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        model.addAttribute("userRoles", userRoles);
        
		// 페이징 처리하고 model에 저장
		applyPagination(incomingByPage, model);

		return VIEW_PATH + "incoming_list";
	}

	// 입고 조회 검색어 포함
	@GetMapping("/incomingListSearch")
	public String incomingListSearch(Model model, @PageableDefault(size = 8) Pageable pageable,
			@RequestParam(name = "itemCodeOrName", defaultValue = "") String itemCodeOrName,
			@RequestParam(name = "reasonOfIncoming", defaultValue = "") String reasonOfIncoming,
			@RequestParam(name = "incomingStartDate_start", defaultValue = "") String incomingStartDate_startStr,
			@RequestParam(name = "incomingStartDate_end", defaultValue = "") String incomingStartDate_endStr,
			@RequestParam(name = "incomingId", defaultValue = "") String incomingId,
			@RequestParam(name = "prodOrOrderId", defaultValue = "") String prodOrOrderId,
			@RequestParam(name = "status", defaultValue = "") String status,
			@RequestParam(name = "managerCodeOrName", defaultValue = "") String managerCodeOrName) {

		log.info("InventoryController incomingListSearch()");
		log.info("넘어온것 :");
		log.info("itemCodeOrName = " + itemCodeOrName);
		log.info("reasonOfIncoming = " + reasonOfIncoming);

		log.info("incomingId = " + incomingId);
		log.info("prodOrOrderId = " + prodOrOrderId);
		log.info("status = " + status);
		log.info("managerCodeOrName = " + managerCodeOrName);

		Timestamp incomingStartDate_start = null;
		Timestamp incomingStartDate_end = null;
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// 문자열로 가져온 등록일검색 날짜는 DB에서 조회할때 TimeStamp 타입으로 변환해야 한다
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
			// 에러시 에러메세지를 model에 추가
			model.addAttribute("errorMessage", "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식을 사용하세요.");
			
	        // 현재 사용자의 권한 정보 추가
	        List<String> userRoles = SecurityUtil.getUserAuthorities().stream()
	                .map(authority -> authority.getAuthority())
	                .collect(Collectors.toList());
	        model.addAttribute("userRoles", userRoles);
	        
			return VIEW_PATH + "incoming_list";
		}

		// 입고된 리스트 검색어 포함 조회
		Page<IncomingDTO> incomingByPage = inventoryService.findIncomingBySearch(itemCodeOrName, reasonOfIncoming,
				incomingStartDate_start, incomingStartDate_end, incomingId, prodOrOrderId, status, managerCodeOrName,
				pageable);

		
		
		// 프론트에서 테이블 데이터 조회시 incomingDTOs.[etc...]로 찾아야함
		model.addAttribute("incomingDTOs", incomingByPage);

		// 검색 조건의 기본값 설정
		model.addAttribute("itemCodeOrName", itemCodeOrName);
		model.addAttribute("reasonOfIncoming", reasonOfIncoming);
		model.addAttribute("incomingStartDate_start", incomingStartDate_startStr);
		model.addAttribute("incomingStartDate_end", incomingStartDate_endStr);
		model.addAttribute("incomingId", incomingId);
		model.addAttribute("prodOrOrderId", prodOrOrderId);
		model.addAttribute("status", status);
		model.addAttribute("managerCodeOrName", managerCodeOrName);


		
		// 페이징 처리하고 model에 저장
		applyPagination(incomingByPage, model);

		return VIEW_PATH + "incoming_list";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 출고 등록
	@GetMapping("/outgoingInsert")
	public String outgoingInsert() {
		log.info("InventroyController outgoingInsertget()");
		

		
		
		return VIEW_PATH + "outgoing_insert";
	}

	// 출고 등록post
	@PostMapping("/outgoingInsert")
	public String outgoingInsertPost(@RequestParam(name = "outgoingInsertCode") String outgoingInsertCode,
							            @RequestParam(name = "reasonOfOutgoing") String reasonOfOutgoing,
							            @RequestParam(name = "managerId") String managerId) {
		log.info("InventroyController outgoingInsertPost()");
		log.info("outgoingInsertCode = " + outgoingInsertCode);
	    log.info("reasonOfOutgoing = " + reasonOfOutgoing);
		log.info("managerId = " + managerId);
		
		//출고등록하기
		
		//11월 13일 변경!! 발주완료되었지만 출고 등록되지 않은 발주번호를 가져와서 출고 테이블에 등록함 + 출고 품목테이블에도 등록함
		inventoryService.insertOutgoing(outgoingInsertCode, reasonOfOutgoing, managerId);
		
		
		
		
		
		return "redirect:/inven/outgoingInsert";
	}
	
	
	
	
	// 출고 조회
	@GetMapping("/outgoingList")
	public String outgoingList(Model model, @PageableDefault(size = 8) Pageable pageable) {
		log.info("InventroyController outgoingList()");

		// 출고된 리스트 1페이지 조회
		Page<OutgoingDTO> outgoingByPage = inventoryService.getOutgoingLists(pageable);
//		log.info("Outgoing DTOs: {}", outgoingByPage.getContent());

		// model에 출고테이블에 출력할 데이터 저장
		model.addAttribute("outgoingDTOs", outgoingByPage);

		// 검색 조건의 기본값 설정
		model.addAttribute("itemCodeOrName", "");
		model.addAttribute("reasonOfOutgoing", "");
		model.addAttribute("outgoingStartDate_start", null);
		model.addAttribute("outgoingStartDate_end", null);
		model.addAttribute("outgoingId", "");
		model.addAttribute("prodOrSaleId", "");
		model.addAttribute("status", "");
		model.addAttribute("managerCodeOrName", "");
		
		// 현재 사용자의 권한 정보 추가
	    List<String> userRoles = SecurityUtil.getUserAuthorities().stream()
	            .map(authority -> authority.getAuthority())
	            .collect(Collectors.toList());
	    model.addAttribute("userRoles", userRoles);

		// 페이징 처리하고 model에 저장
		applyPagination(outgoingByPage, model);

		return VIEW_PATH + "outgoing_list";
	}

	// 출고 조회 검색어 포함
	@GetMapping("/outgoingListSearch")
	public String outgoingListSearch(Model model, @PageableDefault(size = 8) Pageable pageable,
			@RequestParam(name = "itemCodeOrName", defaultValue = "") String itemCodeOrName,
			@RequestParam(name = "reasonOfOutgoing", defaultValue = "") String reasonOfOutgoing,
			@RequestParam(name = "outgoingStartDate_start", defaultValue = "") String outgoingStartDate_startStr,
			@RequestParam(name = "outgoingStartDate_end", defaultValue = "") String outgoingStartDate_endStr,
			@RequestParam(name = "outgoingId", defaultValue = "") String outgoingId,
			@RequestParam(name = "prodOrSaleId", defaultValue = "") String prodOrSaleId,
			@RequestParam(name = "status", defaultValue = "") String status,
			@RequestParam(name = "managerCodeOrName", defaultValue = "") String managerCodeOrName) {

		log.info("InventoryController outgoingListSearch()");
		log.info("넘어온것 :");
		log.info("itemCodeOrName = " + itemCodeOrName);
		log.info("reasonOfOutgoing = " + reasonOfOutgoing);

		log.info("outgoingId = " + outgoingId);
		log.info("prodOrSaleId = " + prodOrSaleId);
		log.info("status = " + status);
		log.info("managerCodeOrName = " + managerCodeOrName);

		Timestamp outgoingStartDate_start = null;
		Timestamp outgoingStartDate_end = null;
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// 문자열로 가져온 등록일검색 날짜는 DB에서 조회할때 TimeStamp 타입으로 변환해야 한다
		try {
			// 시작 날짜 처리
			if (outgoingStartDate_startStr != null && !outgoingStartDate_startStr.isEmpty()) {
				String startDateTimeStr = outgoingStartDate_startStr + " 00:00:00";
				outgoingStartDate_start = Timestamp.valueOf(startDateTimeStr);
			}

			// 종료 날짜 처리
			if (outgoingStartDate_endStr != null && !outgoingStartDate_endStr.isEmpty()) {
				String endDateTimeStr = outgoingStartDate_endStr + " 23:59:59";
				outgoingStartDate_end = Timestamp.valueOf(endDateTimeStr);
			}
		} catch (IllegalArgumentException e) {
			log.error("날짜 변환 오류: " + e.getMessage());
			// 에러시 에러메세지를 model에 추가
			model.addAttribute("errorMessage", "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식을 사용하세요.");
			
	        // 현재 사용자의 권한 정보 추가
	        List<String> userRoles = SecurityUtil.getUserAuthorities().stream()
	                .map(authority -> authority.getAuthority())
	                .collect(Collectors.toList());
	        model.addAttribute("userRoles", userRoles);
	        
			return VIEW_PATH + "outgoing_list";
		}

		// 출고된 리스트 검색어 포함 조회
		Page<OutgoingDTO> outgoingByPage = inventoryService.findOutgoingBySearch(itemCodeOrName, reasonOfOutgoing,
				outgoingStartDate_start, outgoingStartDate_end, outgoingId, prodOrSaleId, status, managerCodeOrName,
				pageable);

		
		
		// 프론트에서 테이블 데이터 조회시 outgoingDTOs.[etc...]로 찾아야함
		model.addAttribute("outgoingDTOs", outgoingByPage);

		// 검색 조건의 기본값 설정
		model.addAttribute("itemCodeOrName", itemCodeOrName);
		model.addAttribute("reasonOfOutgoing", reasonOfOutgoing);
		model.addAttribute("outgoingStartDate_start", outgoingStartDate_startStr);
		model.addAttribute("outgoingStartDate_end", outgoingStartDate_endStr);
		model.addAttribute("outgoingId", outgoingId);
		model.addAttribute("prodOrSaleId", prodOrSaleId);
		model.addAttribute("status", status);
		model.addAttribute("managerCodeOrName", managerCodeOrName);

        // 현재 사용자의 권한 정보 추가
        List<String> userRoles = SecurityUtil.getUserAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        model.addAttribute("userRoles", userRoles);
		
		// 페이징 처리하고 model에 저장
		applyPagination(outgoingByPage, model);

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
