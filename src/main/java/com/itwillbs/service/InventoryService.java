package com.itwillbs.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.itwillbs.entity.Incoming;
import com.itwillbs.entity.IncomingItems;
import com.itwillbs.entity.InventoryItem;
import com.itwillbs.entity.Item;
import com.itwillbs.entity.MFOrder;
import com.itwillbs.entity.Manager;
import com.itwillbs.entity.Order;
import com.itwillbs.entity.OrderItems;
import com.itwillbs.entity.Outgoing;
import com.itwillbs.entity.OutgoingItems;
import com.itwillbs.entity.Sale;
import com.itwillbs.entity.SaleItems;
import com.itwillbs.repository.IncomingItemsRepository;
import com.itwillbs.repository.IncomingRepository;
import com.itwillbs.repository.InventoryRepository;
import com.itwillbs.repository.MFRepository;
import com.itwillbs.repository.ManagerRepository;
import com.itwillbs.repository.OrderRepository;
import com.itwillbs.repository.OutgoingItemsRepository;
import com.itwillbs.repository.OutgoingRepository;
import com.itwillbs.repository.SaleRepository;

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
	private final SaleRepository saleRepository;
	private final MFRepository mfRepository;

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

	// 재고량, 최소필요재고량 업데이트
	@Transactional
	public void updateInventoryItems(List<InventoryItemDTO> InventoryItemDTOList) throws Exception {
		log.info("InventoryService.updateInventoryItems() - InventoryItemDTOList: {}", InventoryItemDTOList);

		for (InventoryItemDTO itemData : InventoryItemDTOList) {
			String itemCode = itemData.getItemCode();
			Integer quantity = itemData.getQuantity();
			Integer minReqQuantity = itemData.getMinReqQuantity();

			InventoryItem item = inventoryRepository.findById(itemCode)
					.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 품목 코드입니다: " + itemCode));
			item.setQuantity(quantity); // 재고량 입력
			item.setMinReqQuantity(minReqQuantity); // 최소필요 재고량 입력
			inventoryRepository.save(item); // 품목 업데이트
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
			Timestamp incomingStartDate_start, Timestamp incomingStartDate_end, String incomingId, String prodOrOrderId,
			String status, String managerCodeOrName, Pageable pageable) {
		log.info("findIncomingBySearch()");

		// 한 번의 쿼리로 Incoming 엔티티와 연관된 데이터를 모두 조회
		Page<Incoming> incomingEntitiesPage = incomingRepository.findIncomingEntities(reasonOfIncoming,
				incomingStartDate_start, incomingStartDate_end, incomingId, prodOrOrderId, status, managerCodeOrName,
				itemCodeOrName, pageable);

		// Incoming 엔티티를 IncomingDTO로 매핑
		Page<IncomingDTO> incomingDTOsPage = incomingEntitiesPage.map(incoming -> {
			IncomingDTO dto = new IncomingDTO(incoming.getIncomingId(), incoming.getIncomingStartDate(),
					incoming.getIncomingEndDate(),
					incoming.getManager() != null ? incoming.getManager().getManagerId() : "",
					incoming.getManager() != null ? incoming.getManager().getName() : "",
					incoming.getStatus(),
					incoming.getMfOrder() != null ? incoming.getMfOrder().getOrderId() : "",
					incoming.getOrder() != null ? incoming.getOrder().getOrderId() : "");

			// 입고 품목 데이터 설정
			List<IncomingItems> itemNames = incoming.getIncomingItems(); // Fetch Join으로 이미 로드됨
			if (!itemNames.isEmpty()) {
				// 첫 번째 품목의 이름을 설정
				dto.setIncomingItemDisplay(itemNames.get(0).getItem().getItemName());
				// 나머지 품목 갯수 설정
				dto.setOtherCount(itemNames.size() - 1);
			} else {
				dto.setIncomingItemDisplay("");
				dto.setOtherCount(0);
			}

			return dto;
		});

		return incomingDTOsPage;
	}

	// 입고 품목 리스트 가져오기
	public List<IncomingItemsDTO> getIncomingItemsDTO(String incomingId) {
		log.info("getIncomingItemsDTO");

		
		List<IncomingItems> incomingItems = incomingItemsRepository.findByIncomingItems(incomingId);
		return incomingItems.stream().map(item -> {
			IncomingItemsDTO dto = new IncomingItemsDTO();
			dto.setItemCode(item.getItem().getItemCode());
			dto.setItemName(item.getItem().getItemName());
			dto.setItemType(item.getItem().getItemType()); // 실제 DB에서 가져온 itemType
			dto.setQuantity(item.getQuantity());
			return dto;
		}).collect(Collectors.toList());
	}

	// 입고 상세 정보 모달창에서 입고 상태 업데이트(입고 완료)
	@Transactional
	public void updateIncomingStatus(String incomingId) {
		Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
		int updatedRows = incomingRepository.updateIncomingStatus(incomingId, currentTime);
		if (updatedRows == 0) {
			throw new EntityNotFoundException("해당 입고 ID가 존재하지 않습니다: " + incomingId);
		}
		
		Optional<Incoming> incomingOpt = incomingRepository.findById(incomingId);
	    if (!incomingOpt.isPresent()) {
	        throw new EntityNotFoundException("해당 입고 ID가 존재하지 않습니다: " + incomingId);
	    }
	    
	    Incoming incoming = incomingOpt.get();
	    
	    // 재고에서 입고된 품목들을 더해줌
	    List<IncomingItems> incomingItems = incoming.getIncomingItems();
	    
	    for (IncomingItems incomingItem : incomingItems) {
	        Item item = incomingItem.getItem();

	        // 품목의 재고 엔티티를 설정
	        InventoryItem inventoryItem = item.getInventoryItem();

	        if (inventoryItem == null) {
	            // 품목의 재고 엔티티가 null이면 새롭게 생성(예외처리)
	            inventoryItem = new InventoryItem();
	            inventoryItem.setItem(item);
	            inventoryItem.setQuantity(0);
	        }

	        int currentQuantity = inventoryItem.getQuantity(); //현재 재고량
	        int incomingQuantity = incomingItem.getQuantity(); //등록할 입고량

	        // 재고량 설정
	        inventoryItem.setQuantity(currentQuantity + incomingQuantity);

	        // 재고량 업데이트
	        inventoryRepository.save(inventoryItem);
	    }
	    
	    
	    //작업번호의 status를 작업완료 → 작업종료로 변경
		if(incomingOpt.get().getMfOrder() != null){
			mfRepository.completeOrder(incomingOpt.get().getMfOrder().getOrderId()); 
		}
	}

	// 입고 등록 페이지에서 입고 대상 가져오기
	public List<IncomingInsertDTO> findIncomingInsertList() {
		log.info("findIncomingInsertList()");
		// 생산 완료가되었지만 아직 입고 등록이 안된 생산데이터 저장
		List<IncomingInsertDTO> incomingInsertDTOProd = incomingRepository.findAllEndOfProduction();

		// 각 생산 완료된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
		// 생산코드 하나에 품목 하나만 있으므로 구현하지 않음. 필요시 구현

		// 발주완료가되었지만 아직 입고 등록이 안된 발주데이터 저장
		List<IncomingInsertDTO> incomingInsertDTOOrder = incomingRepository.findAllEndOfOrder();

		
		log.info("incomingInsertDTOOrder = " + incomingInsertDTOOrder.toString());
	
		// 각 발주완료된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
		// ※최적화 생각해야함
		incomingInsertDTOOrder.forEach(dto -> {

			int totalAmount = 0;
			String OrderId = dto.getProdOrOrderId();
			log.info("dto안임");
			// order_items테이블에서 품목코드와 품목이름을 구함
//			List<IncomingItems> itemNames = incomingItemsRepository.findOrderItemsById(OrderId);
			// order_items테이블에서 품목코드와 품목이름을 구함
			List<IncomingItemsDTO> itemNames = incomingItemsRepository.findOrderItemsById(OrderId);

			if (!itemNames.isEmpty()) {
				// 첫 번째 품목의 이름을 설정
				dto.setIncomingItemDisplay(itemNames.get(0).getItemName());
				// 나머지 품목 갯수 설정
				dto.setOtherCount(itemNames.size() - 1);

				// 총 수량 구하기
				for (IncomingItemsDTO item : itemNames) {
					totalAmount += item.getQuantity();
				}
				dto.setTotalAmount(totalAmount);

			} else {
				dto.setIncomingItemDisplay("");
				dto.setOtherCount(0);
			}
		});

		incomingInsertDTOProd.addAll(incomingInsertDTOOrder);

		log.info("incomingInsertDTOProd = " + incomingInsertDTOProd.toString());

		return incomingInsertDTOProd;
	}

	// 입고 등록페이지에서 선택한 발주/생산 번호의 품목들 찾기
	public List<IncomingItemsDTO> findIncomingInsertItems(String prodOrOrderId, String reasonOfIncoming) {

		if (reasonOfIncoming.equals("작업 완료")) {
			// 생산 테이블에서 조회
			return incomingItemsRepository.findIncomingInsertProdItemsById(prodOrOrderId);
		} else {
			// 발주품목테이블에서 조회
//			return incomingItemsRepository.findOrderItemsById(prodOrOrderId);

			// 발주품목테이블에서 조회
			return incomingItemsRepository.findOrderItemsById(prodOrOrderId);
		}

	}

	// 입고와 입고품목들 등록하기
	@Transactional
	public void insertIncoming(String incomingInsertCode, String reasonOfIncoming, String managerId) {
		log.info("입고 등록");
	    

		// 입고 엔티티 생성 및 설정
	    Incoming incoming = new Incoming();
	    incoming.setIncomingId(generateIncomingId());
	    // 입고 등록일 설정
	    incoming.setIncomingStartDate(new Timestamp(System.currentTimeMillis()));
	    incoming.setStatus("입고 진행중");

	    // Manager 설정
	    Manager manager = new Manager();
	    manager.setManagerId(managerId);
	    incoming.setManager(manager);
		
		// 가져온 코드를 기준으로 작업 또는 발주 데이터 찾기

	    if (reasonOfIncoming.equals("작업 완료")) {
	        // 생산 완료 로직
	        Optional<MFOrder> optionalMFOrder = mfRepository.findById(incomingInsertCode);
	        if (!optionalMFOrder.isPresent()) {
	            log.error("생산 데이터를 찾을 수 없습니다. incomingInsertCode={}", incomingInsertCode);
	            throw new IllegalArgumentException("Invalid incomingInsertCode: " + incomingInsertCode);
	        }

	        MFOrder mfOrder = optionalMFOrder.get();
	        log.info("생산 데이터 조회 완료 : " + mfOrder.toString());

	        
	        // 이제 mfOrder 데이터를 사용하여 incoming 및 incomingItems를 설정
	        IncomingItems incomingItem = new IncomingItems();
	        if (incomingItem.getItem() == null) {
	            incomingItem.setItem(new Item());
	        }
	        incomingItem.setIncomingItemId(generateIncomingItemId());	        
	        incomingItem.getItem().setItemCode(mfOrder.getItem().getItemCode());        
	        incomingItem.setQuantity(mfOrder.getOrderAmount());	        
	        incomingItem.setIncoming(incoming);	        
	        incoming.setMfOrder(mfOrder);
	        incoming.getIncomingItems().add(incomingItem); 
	        
	    } else {
	        // 발주 완료 로직
	        Optional<Order> optionalOrder = orderRepository.findById(incomingInsertCode);
	        if (!optionalOrder.isPresent()) {
	            log.error("발주 데이터를 찾을 수 없습니다. incomingInsertCode={}", incomingInsertCode);
	            throw new IllegalArgumentException("Invalid incomingInsertCode: " + incomingInsertCode);
	        }

	        Order order = optionalOrder.get();
	        log.info("발주 데이터 조회 완료 : " + order.toString());

	        // 이제 order 데이터를 사용하여 incoming 및 incomingItems를 설정
	        createIncomingFromOrder(incoming, order);
	    }
	    
	    
		incomingRepository.save(incoming);
		log.info("입고 데이터 저장 완료: {}", incoming);

	}

	
	private String generateIncomingId() {
	    List<Incoming> incomings = incomingRepository.findAllOrderByNumericIncomingIdDesc();
	    if (incomings.isEmpty()) {
	        return "INC00001"; // 첫 번째 ID
	    } else {
	        String lastId = incomings.get(0).getIncomingId();
	        int numericPart = Integer.parseInt(lastId.substring(3)); 
	        numericPart += 1;
	        return String.format("INC%05d", numericPart); 
	    }
	}

	private String generateIncomingItemId() {
	    return "INCITEM" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}




	private void createIncomingFromOrder(Incoming incoming, Order order) {
	    // OrderItems를 가져와서 IncomingItems를 생성 및 추가
	    List<OrderItems> orderItemsList = order.getOrderItems();
	    for (OrderItems orderItem : orderItemsList) {
	        IncomingItems incomingItem = new IncomingItems();
	        incomingItem.setIncomingItemId(generateIncomingItemId());
	        if (incomingItem.getItem() == null) {
	            incomingItem.setItem(new Item());
	        }
	        log.info("if문 다음");
	        incomingItem.getItem().setItemCode(orderItem.getItem().getItemCode());
	        incomingItem.setQuantity(orderItem.getQuantity());
	
	        // 입고 엔티티와의 관계 설정
	        incomingItem.setIncoming(incoming);
	
	        // 입고 품목 리스트에 추가
	        incoming.getIncomingItems().add(incomingItem);
	        log.info("add 다음");
	    }
	
	    incoming.setOrder(order);
	}
	
	
	
	
	
	
	
	
	
	
	//-----------출고 service 시작------------//
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 출고 페이지 진입시 조회
	public Page<OutgoingDTO> getOutgoingLists(Pageable pageable) {
		log.info("getOutgoingLists()");

		// 페이지 사이즈에 맞는 출고 테이블 데이터 조회
		Page<OutgoingDTO> outgoingByPage = outgoingRepository.getOutgoingLists(pageable);

		// 각 출고데이터마다 품목의 이름과 갯수를 구하기 위한 반복문
		outgoingByPage.forEach(dto -> {

			String outgoingId = dto.getOutgoingId();

			// outgoing_items테이블에서 품목의 이름과 갯수를 구한다.
			List<OutgoingItems> itemNames = outgoingItemsRepository.findOutgoingItemsListById(outgoingId);

			// 품목중 첫번째 품목의 이름을 저장
			dto.setOutgoingItemDisplay(itemNames.get(0).getItem().getItemName());

			// 품목 갯수 - 1을 저장
			dto.setOtherCount(itemNames.size() - 1);

		});

		return outgoingByPage;
	}

	// 출고 목록 검색 (검색 조건과 페이지네이션 포함)
	public Page<OutgoingDTO> findOutgoingBySearch(String itemCodeOrName, String reasonOfOutgoing,
			Timestamp outgoingStartDate_start, Timestamp outgoingStartDate_end, String outgoingId, String prodOrSaleId,
			String status, String managerCodeOrName, Pageable pageable) {
		log.info("findOutgoingBySearch()");

		// 한 번의 쿼리로 Outgoing 엔티티와 연관된 데이터를 모두 조회
		Page<Outgoing> outgoingEntitiesPage = outgoingRepository.findOutgoingEntities(reasonOfOutgoing,
				outgoingStartDate_start, outgoingStartDate_end, outgoingId, prodOrSaleId, status, managerCodeOrName,
				itemCodeOrName, pageable);

		// Outgoing 엔티티를 OutgoingDTO로 매핑
		Page<OutgoingDTO> outgoingDTOsPage = outgoingEntitiesPage.map(outgoing -> {
			OutgoingDTO dto = new OutgoingDTO(outgoing.getOutgoingId(), outgoing.getOutgoingStartDate(),
					outgoing.getOutgoingEndDate(),
					outgoing.getManager() != null ? outgoing.getManager().getManagerId() : "",
					outgoing.getManager() != null ? outgoing.getManager().getName() : "",
					outgoing.getStatus(),
					outgoing.getMfOrder() != null ? outgoing.getMfOrder().getOrderId() : "",
					outgoing.getSale() != null ? outgoing.getSale().getSaleId() : "");

			// 출고 품목 데이터 설정
			List<OutgoingItems> itemNames = outgoing.getOutgoingItems(); // Fetch Join으로 이미 로드됨
			if (!itemNames.isEmpty()) {
				// 첫 번째 품목의 이름을 설정
				dto.setOutgoingItemDisplay(itemNames.get(0).getItem().getItemName());
				// 나머지 품목 갯수 설정
				dto.setOtherCount(itemNames.size() - 1);
			} else {
				dto.setOutgoingItemDisplay("");
				dto.setOtherCount(0);
			}

			return dto;
		});

		return outgoingDTOsPage;
	}

	// 출고 품목 리스트 가져오기
	public List<OutgoingItemsDTO> getOutgoingItemsDTO(String outgoingId) {
		log.info("getOutgoingItemsDTO");
		List<OutgoingItems> outgoingItems = outgoingItemsRepository.findByOutgoingItems(outgoingId);
		return outgoingItems.stream().map(item -> {
			OutgoingItemsDTO dto = new OutgoingItemsDTO();
			dto.setItemCode(item.getItem().getItemCode());
			dto.setItemName(item.getItem().getItemName());
			dto.setItemType(item.getItem().getItemType()); // 실제 DB에서 가져온 itemType
			dto.setQuantity(item.getQuantity());
			return dto;
		}).collect(Collectors.toList());
	}

	// 출고 상세 정보 모달창에서 출고 상태 업데이트(출고 완료)
	@Transactional
	public void updateOutgoingStatus(String outgoingId) {
		Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
		int updatedRows = outgoingRepository.updateOutgoingStatus(outgoingId, currentTime);
		if (updatedRows == 0) {
			throw new EntityNotFoundException("해당 출고 ID가 존재하지 않습니다: " + outgoingId);
		}
		
		Optional<Outgoing> outgoingOpt = outgoingRepository.findById(outgoingId);
	    if (!outgoingOpt.isPresent()) {
	        throw new EntityNotFoundException("해당 출고 ID가 존재하지 않습니다: " + outgoingId);
	    }
		
	    Outgoing outgoing = outgoingOpt.get();
	    
	    // 재고에서 입고된 품목들을 더해줌
	    List<OutgoingItems> outgoingItems = outgoing.getOutgoingItems();
	    
	    for (OutgoingItems outgoingItem : outgoingItems) {
	        Item item = outgoingItem.getItem();

	        // 품목의 재고 엔티티를 설정
	        InventoryItem inventoryItem = item.getInventoryItem();

	        if (inventoryItem == null) {
	            // 품목의 재고 엔티티가 null이면 새롭게 생성(예외처리)
	            inventoryItem = new InventoryItem();
	            inventoryItem.setItem(item);
	            inventoryItem.setQuantity(-1);
	        }

	        int currentQuantity = inventoryItem.getQuantity(); //현재 재고량
	        int outgoingQuantity = outgoingItem.getQuantity(); //등록할 입고량

	        // 재고량 설정
	        inventoryItem.setQuantity(currentQuantity - outgoingQuantity);

	        // 재고량 업데이트
	        inventoryRepository.save(inventoryItem);
	    }
	    
	    
		if(outgoingOpt.get().getMfOrder() != null){//작업번호의 status를 작업대기 → 작업중으로 변경
			mfRepository.startOrder(outgoingOpt.get().getMfOrder().getOrderId()); 
		}
	}

	// 출고 등록 페이지에서 출고 대상 가져오기
	public List<OutgoingInsertDTO> findOutgoingInsertList() {
		log.info("findOutgoingInsertList()");
		// 생산 완료가되었지만 아직 출고 등록이 안된 생산데이터 저장
		List<OutgoingInsertDTO> outgoingInsertDTOProd = outgoingRepository.findAllEndOfProduction();

		// 각 생산 완료된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
		// 생산코드 하나에 품목 하나만 있으므로 구현하지 않음. 필요시 구현

		// 발주완료가되었지만 아직 출고 등록이 안된 발주데이터 저장
		List<OutgoingInsertDTO> outgoingInsertDTOSale = outgoingRepository.findAllEndOfSale();

		
		log.info("outgoingInsertDTOSale = " + outgoingInsertDTOSale.toString());
	
		// 각 발주완료된 데이터행마다 품목의 이름과 갯수를 구하기 위한 반복문
		// ※최적화 생각해야함
		outgoingInsertDTOSale.forEach(dto -> {

			int totalAmount = 0;
			String SaleId = dto.getProdOrSaleId();
			log.info("dto안임");

			// sale_items테이블에서 품목코드와 품목이름을 구함
			List<OutgoingItemsDTO> itemNames = outgoingItemsRepository.findSaleItemsById(SaleId);

			if (!itemNames.isEmpty()) {
				// 첫 번째 품목의 이름을 설정
				dto.setOutgoingItemDisplay(itemNames.get(0).getItemName());
				// 나머지 품목 갯수 설정
				dto.setOtherCount(itemNames.size() - 1);

				// 총 수량 구하기
				for (OutgoingItemsDTO item : itemNames) {
					totalAmount += item.getQuantity();
				}
				dto.setTotalAmount(totalAmount);

			} else {
				dto.setOutgoingItemDisplay("");
				dto.setOtherCount(0);
			}
		});

		outgoingInsertDTOProd.addAll(outgoingInsertDTOSale);

		log.info("outgoingInsertDTOProd = " + outgoingInsertDTOProd.toString());

		return outgoingInsertDTOProd;
	}

	// 출고 등록페이지에서 선택한 발주/생산 번호의 품목들 찾기
	public List<OutgoingItemsDTO> findOutgoingInsertItems(String prodOrSaleId, String reasonOfOutgoing) {

		if (reasonOfOutgoing.equals("작업 대기")) {
			// 생산 테이블에서 조회
			return outgoingItemsRepository.findOutgoingInsertProdItemsById(prodOrSaleId);
		} else {
			// 발주품목테이블에서 조회
//			return outgoingItemsRepository.findSaleItemsById(prodOrSaleId);

			// 발주품목테이블에서 조회
			return outgoingItemsRepository.findSaleItemsById(prodOrSaleId);
		}

	}

	// 출고와 출고품목들 등록하기
	@Transactional
	public void insertOutgoing(String outgoingInsertCode, String reasonOfOutgoing, String managerId) {
		log.info("출고 등록");
	    

		// 출고 엔티티 생성 및 설정
	    Outgoing outgoing = new Outgoing();
	    outgoing.setOutgoingId(generateOutgoingId());
	    // 출고 등록일 설정
	    outgoing.setOutgoingStartDate(new Timestamp(System.currentTimeMillis()));
	    outgoing.setStatus("출고 진행중");

	    // Manager 설정
	    Manager manager = new Manager();
	    manager.setManagerId(managerId);
	    outgoing.setManager(manager);
		
		// 가져온 코드를 기준으로 작업 또는 발주 데이터 찾기

	    if (reasonOfOutgoing.equals("작업 대기")) {
	        // 생산 완료 로직
	        Optional<MFOrder> optionalMFOrder = mfRepository.findById(outgoingInsertCode);
	        if (!optionalMFOrder.isPresent()) {
	            log.error("생산 데이터를 찾을 수 없습니다. outgoingInsertCode={}", outgoingInsertCode);
	            throw new IllegalArgumentException("Invalid outgoingInsertCode: " + outgoingInsertCode);
	        }

	        MFOrder mfOrder = optionalMFOrder.get();
	        log.info("생산 데이터 조회 완료 : " + mfOrder.toString());

	        
	        // 이제 mfOrder 데이터를 사용하여 outgoing 및 outgoingItems를 설정
	        OutgoingItems outgoingItem = new OutgoingItems();
	        if (outgoingItem.getItem() == null) {
	            outgoingItem.setItem(new Item());
	        }
	        outgoingItem.setOutgoingItemId(generateOutgoingItemId());	        
	        outgoingItem.getItem().setItemCode(mfOrder.getItem().getItemCode());        
	        outgoingItem.setQuantity(mfOrder.getOrderAmount());	        
	        outgoingItem.setOutgoing(outgoing);	        
	        outgoing.setMfOrder(mfOrder);
	        outgoing.getOutgoingItems().add(outgoingItem); 
	        
	    } else {
	        // 수주 완료 로직
	        Optional<Sale> optionalSale = saleRepository.findById(outgoingInsertCode);
	        if (!optionalSale.isPresent()) {
	            log.error("수주 데이터를 찾을 수 없습니다. outgoingInsertCode={}", outgoingInsertCode);
	            throw new IllegalArgumentException("Invalid outgoingInsertCode: " + outgoingInsertCode);
	        }

	        Sale sale = optionalSale.get();
	        log.info("발주 데이터 조회 완료 : " + sale.toString());

	        // 이제 sale 데이터를 사용하여 outgoing 및 outgoingItems를 설정
	        createOutgoingFromSale(outgoing, sale);
	    }
	    
	    
		outgoingRepository.save(outgoing);
		log.info("출고 데이터 저장 완료: {}", outgoing);

	}

	
	private String generateOutgoingId() {
	    List<Outgoing> outgoings = outgoingRepository.findAllOrderByNumericOutgoingIdDesc();
	    if (outgoings.isEmpty()) {
	        return "OUT00001"; // 첫 번째 ID
	    } else {
	        String lastId = outgoings.get(0).getOutgoingId();
	        int numericPart = Integer.parseInt(lastId.substring(3)); 
	        numericPart += 1;
	        return String.format("OUT%05d", numericPart); 
	    }
	}

	private String generateOutgoingItemId() {
	    return "OUTITEM" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}




	private void createOutgoingFromSale(Outgoing outgoing, Sale sale) {
	    // SaleItems를 가져와서 OutgoingItems를 생성 및 추가
	    List<SaleItems> saleItemsList = sale.getSaleItems();
	    for (SaleItems saleItem : saleItemsList) {
	        OutgoingItems outgoingItem = new OutgoingItems();
	        outgoingItem.setOutgoingItemId(generateOutgoingItemId());
	        if (outgoingItem.getItem() == null) {
	            outgoingItem.setItem(new Item());
	        }
	        log.info("if문 다음");
	        outgoingItem.getItem().setItemCode(saleItem.getItem().getItemCode());
	        outgoingItem.setQuantity(saleItem.getQuantity());
	
	        // 출고 엔티티와의 관계 설정
	        outgoingItem.setOutgoing(outgoing);
	
	        // 출고 품목 리스트에 추가
	        outgoing.getOutgoingItems().add(outgoingItem);
	        log.info("add 다음");
	    }
	
	    outgoing.setSale(sale);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 권한이 있는 관리자 찾기
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


	
}


