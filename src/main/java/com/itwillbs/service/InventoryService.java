package com.itwillbs.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingInsertDTO;
import com.itwillbs.domain.inventory.IncomingItemsDTO;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.domain.inventory.OutgoingDTO;
import com.itwillbs.domain.inventory.OutgoingInsertDTO;
import com.itwillbs.domain.inventory.OutgoingItemsDTO;
import com.itwillbs.domain.transaction.OrderDTO;
import com.itwillbs.entity.Incoming;
import com.itwillbs.entity.IncomingItems;
import com.itwillbs.entity.InventoryItem;
import com.itwillbs.entity.Manager;
import com.itwillbs.entity.Order;
import com.itwillbs.entity.OrderItems;
import com.itwillbs.repository.IncomingItemsRepository;
import com.itwillbs.repository.IncomingRepository;
import com.itwillbs.repository.InventoryRepository;
import com.itwillbs.repository.ManagerRepository;
import com.itwillbs.repository.OrderRepository;
import com.itwillbs.repository.OutgoingItemsRepository;
import com.itwillbs.repository.OutgoingRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
	private final InventoryRepository inventoryRepository;
	private final IncomingRepository incomingRepository;
	private final IncomingItemsRepository incomingItemsRepository;
	private final OutgoingRepository outgoingRepository;
	private final OutgoingItemsRepository outgoingItemsRepository;
	private final ManagerRepository managerRepository;
	private final OrderRepository orderRepository;
	
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
	public Page<InventoryItemDTO> findInventoryItemsBySearch(String itemCodeOrName, String itemType,
			Pageable pageable) {
		log.info("findInventoryItems()");
		return inventoryRepository.findInventoryItems(itemCodeOrName, itemType, pageable);
	}

	//재고량, 최소필요재고량 업데이트
	@Transactional
	public void updateInventoryItems(List<InventoryItemDTO> InventoryItemDTOList) throws Exception {
		log.info("InventoryService.updateInventoryItems() - InventoryItemDTOList: {}", InventoryItemDTOList);

		for (InventoryItemDTO itemData : InventoryItemDTOList) {
			String itemCode = itemData.getItemCode();
			Integer quantity = itemData.getQuantity();
			Integer minReqQuantity = itemData.getMinReqQuantity();

			InventoryItem item = inventoryRepository.findById(itemCode)
					.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 품목 코드입니다: " + itemCode));
			item.setQuantity(quantity); //재고량 입력
			item.setMinReqQuantity(minReqQuantity); //최소필요 재고량 입력
			inventoryRepository.save(item); //품목 업데이트
		}
	}
	
	// 입고 페이지 진입시 조회
	public Page<IncomingDTO> getIncomingLists(Pageable pageable) {
		log.info("getIncomingLists()");

		// 페이지 사이즈에 맞는 입고 테이블 데이터 조회
		Page<IncomingDTO> incomingByPage = incomingRepository.getIncomingLists(pageable);

		// 각 입고데이터마다 품목의 이름과 갯수를 구하기 위한 반복문
		incomingByPage.forEach(dto -> {

			String incomingId = dto.getIncomingId();

			// incoming_items테이블에서 품목의 이름과 갯수를 구한다.
			List<IncomingItems> itemNames = incomingItemsRepository.findIncomingItemsListById(incomingId);

			// 품목중 첫번째 품목의 이름을 저장
			dto.setIncomingItemDisplay(itemNames.get(0).getItem().getItemName());

			// 품목 갯수 - 1을 저장
			dto.setOtherCount(itemNames.size() - 1);

		});

		return incomingByPage;
	}

	// 입고 목록 검색 (검색 조건과 페이지네이션 포함)
	public Page<IncomingDTO> findIncomingBySearch(String itemCodeOrName, String reasonOfIncoming,

			Timestamp incomingStartDate_start, Timestamp incomingStartDate_end, String incomingId, String prodOrQualId,
			String status, String managerCodeOrName, Pageable pageable) {
		log.info("findIncomingBySearch()");

		// 리포지토리 호출 시 itemCodeOrName 포함
		Page<IncomingDTO> incomingByPage = incomingRepository.findIncomingLists(reasonOfIncoming,
				incomingStartDate_start, incomingStartDate_end, incomingId, prodOrQualId, status, managerCodeOrName,
				itemCodeOrName, pageable);

		// 각 입고 데이터마다 품목의 이름과 갯수를 구하기 위한 반복문
		// ※최적화 생각해야함
		incomingByPage.forEach(dto -> {

			String incomingId2 = dto.getIncomingId();

			// incoming_items 테이블에서 품목코드와 품목이름을 구함
			List<IncomingItems> itemNames = incomingItemsRepository.findIncomingItemsListById(incomingId2);

			if (!itemNames.isEmpty()) {
				// 첫 번째 품목의 이름을 설정
				dto.setIncomingItemDisplay(itemNames.get(0).getItem().getItemName());
				// 나머지 품목 갯수 설정
				dto.setOtherCount(itemNames.size() - 1);
			} else {
				dto.setIncomingItemDisplay("");
				dto.setOtherCount(0);
			}
		});

		return incomingByPage;
	}

	// 입고 품목 리스트 가져오기
	public List<IncomingItems> getIncomingItems(String incomingId) {

		return incomingItemsRepository.findByIncomingItems(incomingId);
	}

	// 입고 상세 정보 모달창에서 입고 상태 업데이트
	@Transactional
	public void updateIncomingStatus(String incomingId) {
		Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
		int updatedRows = incomingRepository.updateIncomingStatus(incomingId, currentTime);
		if (updatedRows == 0) {
			throw new EntityNotFoundException("해당 입고 ID가 존재하지 않습니다: " + incomingId);
		}
	}

	// 입고 등록 페이지에서 입고 대상 가져오기
	public List<IncomingInsertDTO> findIncomingInsertList() {
		log.info("findIncomingInsertList()");
		// 생산 완료가되었지만 아직 입고 등록이 안된 생산데이터 저장
		List<IncomingInsertDTO> incomingInsertDTOProd = incomingRepository.findAllEndOfProduction();

		// 각 생산 완료된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
		// 생산코드 하나에 품목 하나만 있으므로 구현하지 않음. 필요시 구현

		// 입하 검품완료가되었지만 아직 입고 등록이 안된 검품데이터 저장
		List<IncomingInsertDTO> incomingInsertDTOQual = incomingRepository.findAllEndOfQuality();

		// 각 검품완료된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
		// ※최적화 생각해야함
		incomingInsertDTOQual.forEach(dto -> {

			int totalAmount = 0;
			String QualityOrderId = dto.getProdOrQualId();

			// quality_order_items테이블에서 품목코드와 품목이름을 구함
//			List<IncomingItems> itemNames = incomingItemsRepository.findQualityOrderItemsById(QualityOrderId);
			// order_items테이블에서 품목코드와 품목이름을 구함
			List<IncomingItems> itemNames = incomingItemsRepository.findOrderItemsById(QualityOrderId);
			
			if (!itemNames.isEmpty()) {
				// 첫 번째 품목의 이름을 설정
				dto.setIncomingItemDisplay(itemNames.get(0).getItem().getItemName());
				// 나머지 품목 갯수 설정
				dto.setOtherCount(itemNames.size() - 1);
				
				//총 수량 구하기
				for (IncomingItems item : itemNames) {
					totalAmount += item.getQuantity();
				} 
				dto.setTotalAmount(totalAmount);	
				
			} else {
				dto.setIncomingItemDisplay("");
				dto.setOtherCount(0);
			}
		});

		incomingInsertDTOProd.addAll(incomingInsertDTOQual);

		log.info("incomingInsertDTOProd = " + incomingInsertDTOProd.toString());
		
		return incomingInsertDTOProd;
	}

	//입고 등록페이지에서 선택한 검품/생산 번호의 품목들 찾기
	public List<IncomingItems> findIncomingInsertItems(String prodOrQualId, String reasonOfIncoming) {
		
		if(reasonOfIncoming.equals("작업 완료")){
			//생산 테이블에서 조회
			return incomingItemsRepository.findIncomingInsertProdItemsById(prodOrQualId);
		}else {
			// 입하 검품품목테이블에서 조회
//			return incomingItemsRepository.findQualityOrderItemsById(prodOrQualId);
			
			// 발주품목테이블에서 조회
			return incomingItemsRepository.findOrderItemsById(prodOrQualId);
		}
		
		
	}

	//입고와 입고품목들 등록하기
	@Transactional
	public void insertIncoming(String incomingInsertCode, String managerId) {
		log.info("입고 등록");
		
		//가져온 발주코드를 기준으로 발주데이터 찾기
		Optional<Order> optionalOrder = orderRepository.findById("OD0001");
		if(!optionalOrder.isPresent()) {
			 log.error("발주 데이터를 찾을 수 없습니다. incomingInsertCode={}", incomingInsertCode);
			 throw new IllegalArgumentException("Invalid incomingInsertCode: " + incomingInsertCode);
		}
		
		 
		Order order = optionalOrder.get();
		log.info("발주 데이터 조회 완료 : " + order);
		
		//입고 엔티티 생성 및 설정
		Incoming incoming = new Incoming();
		
		incoming.setIncomingId(generateIncomingId());
		incoming.setIncomingStartDate(new Timestamp(System.currentTimeMillis()));
		incoming.getManager().setManagerId(managerId);
		incoming.setStatus("입고 진행중");
		
		
		//발주 품목을 가져와서 입고 품목에 생성 및 설정
		List<OrderItems> orderItemsList = order.getOrderItems();
		for (OrderItems orderItem : orderItemsList) {
			IncomingItems incomingItem = new IncomingItems();
            incomingItem.setIncomingItemId(generateIncomingItemId());
            incomingItem.getItem().setItemCode(orderItem.getItem().getItemCode());
            incomingItem.setQuantity(orderItem.getQuantity());
            

            // 입고 엔티티와의 관계 설정
            incomingItem.setIncoming(incoming);

            // 입고 품목 리스트에 추가
            incoming.getIncomingItems().add(incomingItem);
		}
		
		incomingRepository.save(incoming);
		log.info("입고 데이터 저장 완료: {}", incoming);
		
	}

	
	
	
	
	
	
	
	
	
	
	// 출고 페이지 진입시 조회
		public Page<OutgoingDTO> getOutgoingLists(Pageable pageable) {
			log.info("getOutgoingLists()");

			// 페이지 사이즈에 맞는 출고 테이블 데이터 조회
			Page<OutgoingDTO> outgoingByPage = outgoingRepository.getOutgoingLists(pageable);

			// 각 출고데이터마다 품목의 이름과 갯수를 구하기 위한 반복문
			outgoingByPage.forEach(dto -> {

				String outgoingId = dto.getOutgoingId();

				// outgoing_items테이블에서 품목의 이름과 갯수를 구한다.
				List<OutgoingItemsDTO> itemNames = outgoingItemsRepository.findOutgoingItemsListById(outgoingId);

				// 품목중 첫번째 품목의 이름을 저장
				dto.setOutgoingItemDisplay(itemNames.get(0).getItemName());

				// 품목 갯수 - 1을 저장
				dto.setOtherCount(itemNames.size() - 1);

			});

			return outgoingByPage;
		}

		// 출고 목록 검색 (검색 조건과 페이지네이션 포함)
		public Page<OutgoingDTO> findOutgoingBySearch(String itemCodeOrName, String reasonOfOutgoing,

				Timestamp outgoingStartDate_start, Timestamp outgoingStartDate_end, String outgoingId, String prodOrQualId,
				String status, String managerCodeOrName, Pageable pageable) {
			log.info("findOutgoingBySearch()");

			// 리포지토리 호출 시 itemCodeOrName 포함
			Page<OutgoingDTO> outgoingByPage = outgoingRepository.findOutgoingLists(reasonOfOutgoing,
					outgoingStartDate_start, outgoingStartDate_end, outgoingId, prodOrQualId, status, managerCodeOrName,
					itemCodeOrName, pageable);

			// 각 출고 데이터마다 품목의 이름과 갯수를 구하기 위한 반복문
			// ※최적화 생각해야함
			outgoingByPage.forEach(dto -> {

				String outgoingId2 = dto.getOutgoingId();

				// outgoing_items 테이블에서 품목코드와 품목이름을 구함
				List<OutgoingItemsDTO> itemNames = outgoingItemsRepository.findOutgoingItemsListById(outgoingId2);

				if (!itemNames.isEmpty()) {
					// 첫 번째 품목의 이름을 설정
					dto.setOutgoingItemDisplay(itemNames.get(0).getItemName());
					// 나머지 품목 갯수 설정
					dto.setOtherCount(itemNames.size() - 1);
				} else {
					dto.setOutgoingItemDisplay("");
					dto.setOtherCount(0);
				}
			});

			return outgoingByPage;
		}

		// 출고 품목 리스트 가져오기
		public List<OutgoingItemsDTO> getOutgoingItems(String outgoingId) {

			return outgoingItemsRepository.findByOutgoingItems(outgoingId);
		}

		// 출고 상세 정보 모달창에서 출고 상태 업데이트
		@Transactional
		public void updateOutgoingStatus(String outgoingId) {
			Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
			int updatedRows = outgoingRepository.updateOutgoingStatus(outgoingId, currentTime);
			if (updatedRows == 0) {
				throw new EntityNotFoundException("해당 출고 ID가 존재하지 않습니다: " + outgoingId);
			}
		}

		// 출고 등록 페이지에서 출고 대상 가져오기
		public List<OutgoingInsertDTO> findOutgoingInsertList() {

			// 생산 완료가되었지만 아직 출고 등록이 안된 생산데이터 저장
			List<OutgoingInsertDTO> outgoingInsertDTOProd = outgoingRepository.findAllEndOfProduction();

			// 각 생산 요청된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
			// 생산코드 하나에 품목 하나만 있으므로 구현하지 않음. 필요시 구현

			// 입하 검품완료가되었지만 아직 출고 등록이 안된 검품데이터 저장
			List<OutgoingInsertDTO> outgoingInsertDTOQual = outgoingRepository.findAllEndOfQuality();

			// 각 검품완료된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
			// ※최적화 생각해야함
//			outgoingInsertDTOQual.forEach(dto -> {
//
//				String QualitySaleId = dto.getOutgoingInsertCode();
//
//				// quality_sale_items테이블에서 품목코드와 품목이름을 구함
//				List<OutgoingItemsDTO> itemNames = outgoingItemsRepository.findQualitySaleItemsById(QualitySaleId);
//
//				if (!itemNames.isEmpty()) {
//					// 첫 번째 품목의 이름을 설정
//					dto.setOutgoingItemDisplay(itemNames.get(0).getItemName());
//					// 나머지 품목 갯수 설정
//					dto.setOtherCount(itemNames.size() - 1);
//				} else {
//					dto.setOutgoingItemDisplay("");
//					dto.setOtherCount(0);
//				}
//			});

			outgoingInsertDTOProd.addAll(outgoingInsertDTOQual);

			return null;
		}

		//권한이 있는 관리자 찾기
		public List<Manager> findManager() {
		    log.info("findManager()");
		    List<Manager> allManagers = managerRepository.findAll();
		    List<Manager> filteredManagers = new ArrayList<>();
		    
		    for (Manager manager : allManagers) {
		        if (manager.getManagerRole().contains("INVENTORY") || manager.getManagerRole().contains("ADMIN")) {
		            filteredManagers.add(manager);
		        }
		    }
		    
		    log.info(filteredManagers.toString());
		    return filteredManagers;
		}

	
		private String generateIncomingId() {
		    // 현재 최대 incoming ID 조회
		    Optional<Incoming> lastIncoming = incomingRepository.findTopByOrderByIncomingIdDesc();
		    if (lastIncoming.isPresent()) {
		        String lastId = lastIncoming.get().getIncomingId();
		        int numericPart = Integer.parseInt(lastId.substring(3)); // "INC" 이후 숫자 부분 추출
		        numericPart += 1;
		        return String.format("INC%05d", numericPart); // "INC" + 5자리 숫자
		    } else {
		        return "INC00001"; // 첫 번째 ID
		    }
		}
	
	    private String generateIncomingItemId() {
	        // 현재 최대 IncomingItem ID 조회
	        Optional<IncomingItems> lastIncomingItem = incomingItemsRepository.findTopByOrderByIncomingItemIdDesc();
	        if (lastIncomingItem.isPresent()) {
	            String lastId = lastIncomingItem.get().getIncomingItemId();
	            int numericPart = Integer.parseInt(lastId.substring(7)); // "INCITEM" 이후 숫자 부분 추출
	            numericPart += 1;
	            return String.format("INCITEM%05d", numericPart); // "INCITEM" + 4자리 숫자
	        } else {
	            return "INCITEM00001"; // 첫 번째 ID
	        }
	    }
	
	
	
	
	
	
	
	
	
	


}
