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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.config.security.util.SecurityUtil;
import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingInsertDTO;
import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.domain.inventory.InvenResponseMessage;
import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.domain.inventory.OutgoingInsertDTO;
import com.itwillbs.domain.inventory.OutgoingItemsDTO;
import com.itwillbs.entity.Incoming;
import com.itwillbs.entity.IncomingItems;
import com.itwillbs.entity.Manager;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restInven")
@Slf4j
@RequiredArgsConstructor
public class RestInventoryController {

	private final InventoryService inventoryService;

	

    /**
     * 재고량 및 최소 필요 재고량 배치 업데이트 엔드포인트
     */
    @PostMapping("/updateInventoryItems")
    public ResponseEntity<InvenResponseMessage> updateInventoryItems(@RequestBody List<InventoryItemDTO> InventoryItemDTOList) {
        log.info("RestInventoryController.updateInventoryItems() - InventoryItemDTOList: {}", InventoryItemDTOList);

        try {
            inventoryService.updateInventoryItems(InventoryItemDTOList);
            InvenResponseMessage response = new InvenResponseMessage(true, "재고 정보가 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.error("재고 정보 수정 실패: {}", e.getMessage());
            InvenResponseMessage response = new InvenResponseMessage(false, "해당 품목의 재고 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("서버 오류: {}", e.getMessage());
            InvenResponseMessage response = new InvenResponseMessage(false, "재고 정보 수정 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
	
 // 입고 상세 정보 ajax
    @GetMapping("/incomingDetail")
    public ResponseEntity<List<IncomingItemsDTO>> getIncomingDetail(@RequestParam(name = "incomingId") String incomingId) {
        log.info("RestInventoryController.getIncomingDetail()");

        // 입고 품목 DTO 리스트 가져오기
        List<IncomingItemsDTO> incomingItemsDTOs = inventoryService.getIncomingItemsDTO(incomingId);

        // itemType 매핑 (DTO에서 직접 설정)
        incomingItemsDTOs.forEach(dto -> {
            switch (dto.getItemType()) {
                case "FP":
                    dto.setItemType("완제품");
                    break;
                case "RM":
                    dto.setItemType("원재료");
                    break;
                case "PP":
                    dto.setItemType("가공품");
                    break;
                default:
                    dto.setItemType("알 수 없음");
            }
        });

        // 입고 품목이 존재하면 반환
        if (!incomingItemsDTOs.isEmpty()) {
            return ResponseEntity.ok(incomingItemsDTOs);
        } else {
            log.info("입고 품목 정보가 없습니다. incomingId: " + incomingId);
            return ResponseEntity.notFound().build();
        }
    }

	//입고 상태 업데이트
	@PostMapping("/updateIncomingStatus")
	public ResponseEntity<InvenResponseMessage> updateIncomingStatus(@RequestParam("incomingId") String incomingId) {
		log.info("RestInventoryController.updateIncomingStatus()");

	      // 현재 사용자 권한 확인
        List<String> userRoles = SecurityUtil.getUserAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
		
        if (!(userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_INVENTORY"))) {
            InvenResponseMessage response = new InvenResponseMessage(false, "권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); //403 에러
        }
		
		
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


		//생산 : status = '생산완료'이고 입고 등록되지 않은 생산번호를 기준으로 가져옴
		//발주 : 입고번호의 status= '발주완료'이고 입고번호의 품목들의 status = '통과'인 발주번호를 가져옴  
    	List<IncomingInsertDTO> incomingInsertDTO = inventoryService.findIncomingInsertList();

		// 입고 등록할 목록이 존재하면 반환
		if (!incomingInsertDTO.isEmpty()) {
			return ResponseEntity.ok(incomingInsertDTO);
		} else {
			log.info("입고 등록할 품목이 존재하지 않습니다.");
			return ResponseEntity.ok(incomingInsertDTO);
		}
	}
	
	// 입고 등록할 목록중 하나 선택해서 그 목록의 품목들을 보여줌
	@GetMapping("/getIncomingInsertItems")
	public ResponseEntity<List<IncomingItems>> getIncomingInsertItems(@RequestParam("prodOrOrderId") String prodOrOrderId,
																	@RequestParam("reasonOfIncoming") String reasonOfIncoming){
		log.info("RestInventoryController.incomingInsertItems()");
		
		List<IncomingItems> incomingItems = inventoryService.findIncomingInsertItems(prodOrOrderId, reasonOfIncoming);
		log.info(incomingItems.toString());
		// 품목이 존재하면 반환
		if (!incomingItems.isEmpty()) {
			return ResponseEntity.ok(incomingItems);
		} else {
			log.info("입고 등록할 품목이 존재하지 않습니다.");
			return ResponseEntity.ok(incomingItems);
		}
	}

	
	


	
	
	
	//출고 상세 정보 ajax
		@GetMapping("/outgoingDetail")
		public ResponseEntity<List<OutgoingItemsDTO>> getOutgoingDetail(
				@RequestParam(name = "outgoingId") String outgoingId) {
			log.info("RestInventoryController.getOutgoingDetail()");
			// 출고 품목 리스트 가져오기
			List<OutgoingItemsDTO> outgoingItemsDTO = inventoryService.getOutgoingItems(outgoingId);

			// itemType 매핑
			outgoingItemsDTO.forEach(item -> {
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

			// 출고 품목이 존재하면 반환
			if (!outgoingItemsDTO.isEmpty()) {
				return ResponseEntity.ok(outgoingItemsDTO);
			} else {
				log.info("출고 품목 정보가 없습니다. outgoingId: " + outgoingId);
				return ResponseEntity.notFound().build();
			}
		}

		//출고 상태 업데이트
		@PostMapping("/updateOutgoingStatus")
		public ResponseEntity<InvenResponseMessage> updateOutgoingStatus(@RequestParam("outgoingId") String outgoingId) {
			log.info("RestInventoryController.updateOutgoingStatus()");

			// 업데이트 실행, 성공or에러시 적절한 메세지를 프론트로 넘김
			try {
				inventoryService.updateOutgoingStatus(outgoingId);
				InvenResponseMessage response = new InvenResponseMessage(true, "출고 완료되었습니다.");
				return ResponseEntity.ok(response);
			} catch (EntityNotFoundException e) {
				InvenResponseMessage response = new InvenResponseMessage(false, "해당 출고 ID의 출고 상태를 업데이트할 수 없습니다.");
				// 400에러
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			} catch (Exception e) {
				InvenResponseMessage response = new InvenResponseMessage(false, "서버 오류가 발생했습니다.");
				// 500에러
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		// 출고 등록할 목록들 조회
		@GetMapping("/outgoingInsertList")
		public ResponseEntity<List<OutgoingInsertDTO>> outgoingInsertList() {
			log.info("RestInventoryController.outgoingInsertList()");

			List<OutgoingInsertDTO> outgoingInsertDTO = new ArrayList<>();
//	    	List<OutgoingInsertDTO> outgoingInsertDTO = inventoryService.findOutgoingInsertList();

			// 출고 등록할 품목이 존재하면 반환
			if (!outgoingInsertDTO.isEmpty()) {
				return ResponseEntity.ok(outgoingInsertDTO);
			} else {
				log.info("출고 등록할 품목이 존재하지 않습니다.");
				return ResponseEntity.ok(outgoingInsertDTO);
			}
		}


		//INVEN 권한 있는 매니저 조회
		@GetMapping("/findManagerList")
		public ResponseEntity<List<Manager>> findManagerList() {
			log.info("RestInventoryController.findManagerList()");
			
			
			//재고관리 권한이 있는 매니저 찾기(admin, inventory)
			List<Manager> managers = inventoryService.findManager();

			// 입고 등록할 품목이 존재하면 반환
			if (!managers.isEmpty()) {
				return ResponseEntity.ok(managers);
			} else {
				log.info("관리자 정보를 찾을 수 없습니다.");
				return ResponseEntity.ok(managers);
			}
		}
	

}
