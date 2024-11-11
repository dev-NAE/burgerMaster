package com.itwillbs.service;

import com.itwillbs.domain.transaction.*;
import com.itwillbs.entity.*;
import com.itwillbs.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Service
public class TXService {

    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ManagerRepository managerRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;
    private final SaleRepository saleRepository;
    private final SaleItemsRepository saleItemsRepository;
    private final FranchiseRepository franchiseRepository;
    private final ShipmentRepository shipmentRepository;

    @Transactional
    public void saveOrder(OrderDTO orderDTO, List<OrderItemsDTO> orderItems) {
        log.info("TXService: saveOrder");
        // 발주번호 생성
        String orderId = generateNextOrderId();

        // 발주 정보 저장
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);      // orderDTO -> order 필드값 복사
        order.setOrderId(orderId);                        // 발주등록번호
        order.setStatus("발주등록(저장)");                  // 발주상태
        order.setRealDate(new Timestamp(System.currentTimeMillis()));
        order.setManager(managerRepository.findById(orderDTO.getManager()).orElse(null));
        order.setSupplier(supplierRepository.findById(orderDTO.getSupplierCode()).orElse(null));
        orderRepository.save(order);

        // 발주 품목정보 저장
        saveOrderItems(orderItems, orderId, order);
    }

    private void saveOrderItems(List<OrderItemsDTO> orderItems, String orderId, Order order) {
        for (OrderItemsDTO item : orderItems) {
            OrderItems orderItem = new OrderItems();
            BeanUtils.copyProperties(item, orderItem);
            orderItem.setItem(itemRepository.findById(item.getItemCode()).orElse(null));
            orderItem.setOrder(order);
            orderItem.setOrderItemId(orderId + item.getItemCode());
            log.info(orderItem.toString());
            orderItemsRepository.save(orderItem);
        }
    }

    public boolean checkValidation(OrderDTO orderDTO) {
        String managerId = orderDTO.getManager();
        String supplierCode = orderDTO.getSupplierCode();
        Optional<Manager> manager = managerRepository.findById(managerId);
        Optional<Supplier> supplier = supplierRepository.findById(supplierCode);
        // 입력한 매니저아이디, 거래처코드가 DB에 존재하는 값인지 검증
        return manager.isPresent() && supplier.isPresent();
    }

    public String generateNextOrderId() {
        String maxOrderId = orderRepository.findMaxOrderId();

        String newOrderId = null;

        if (maxOrderId == null) {
            newOrderId = "OD0001";
        } else {
            int numberPart = Integer.parseInt(maxOrderId.substring(2));
            newOrderId = String.format("OD%04d", numberPart + 1);
        }
        log.info("TXService: generateNextOrderId + newOrderId = " + newOrderId);

        return newOrderId;
    }

    // 접속한 매니저 불러오기 용도
    public String getManagerName(String managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        if (manager.isPresent()) {
            return manager.get().getManagerId();
        } else {
            throw new NoSuchElementException(managerId + "에 해당하는 매니저 없음");
        }
    }

    // 매니저 설정을 위한 검색 용도
    public List<Manager> findManagers(String managerName) {
        if (managerName != null) {
            managerName = "%" + managerName + "%";
        }
        return managerRepository.findManagerOnTX(managerName);
    }

    public List<Supplier> findSuppliers(String supplierName) {
        if (supplierName != null) {
            supplierName = "%" + supplierName + "%";
        }
        return supplierRepository.findSupplierOnTX(supplierName);
    }

    public List<TxItemsDTO> getTXItems(String itemName, List<String> itemTypes) {
        if (itemName != null) {
            itemName = "%" + itemName + "%";
        }
        return itemRepository.findItemsOnTX(itemName, itemTypes);
        // 사용중인 코드 + 구매할 수 있는 아이템 한정, 재고수량 오름차순으로 수정 필요
    }

    public List<OrderDTO> getOrderList() {

        log.info("TXService: getOrderList");
        List<Order> allOrders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderId"));

        return getOrderDTOS(allOrders);

    }

    public Order getOrderById(String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new NoSuchElementException(orderId + "에 해당하는 발주 없음");
        }
    }

    public List<OrderItems> getOrderedItems(Order order) {
        return orderItemsRepository.findByOrder(order);
    }

    public List<OrderDTO> searchOrders(String status, String supplierName, String orderDateStart, String orderDateEnd,
                                       String itemName, String dueDateStart, String dueDateEnd) {
        log.info("TXService: searchOrders");

        // 날짜 자료형 String -> Timestamp 변경
        Timestamp orderStart = convertToTimestamp(orderDateStart);
        Timestamp orderEnd = convertToTimestamp(orderDateEnd);
        Timestamp dueStart = convertToTimestamp(dueDateStart);
        Timestamp dueEnd = convertToTimestamp(dueDateEnd);

        String formattedStatus = (status != null && !status.trim().isEmpty()) ? status : null;

        // LIKE 검색할 것들 % 붙여주기
        String formattedSupplierName = supplierName != null && !supplierName.trim().isEmpty() ? "%" + supplierName + "%" : null;
        String formattedItemName = itemName != null && !itemName.trim().isEmpty() ? "%" + itemName + "%" : null;

        log.info("status: " + formattedStatus + " supplierName: " + formattedSupplierName + " orderDateStart: " + orderStart + " orderDateEnd: " + orderEnd + " itemName: " + formattedItemName + " dueStart: " + dueStart + " dueEnd: " + dueEnd);

        List<Order> ordersByConditions = orderRepository.findOrdersByConditions
                (formattedStatus, formattedSupplierName, orderStart, orderEnd, formattedItemName, dueStart, dueEnd);

        log.info("TXService: searchOrdersByConditions" + ordersByConditions);

        return getOrderDTOS(ordersByConditions);
    }

    private Timestamp convertToTimestamp(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        LocalDate dateTime = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Timestamp.valueOf(dateTime.atStartOfDay());
    }

    private List<OrderDTO> getOrderDTOS(List<Order> ordersByConditions) {
        return ordersByConditions.stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setOrderId(order.getOrderId());
                    orderDTO.setTotalPrice(order.getTotalPrice());
                    orderDTO.setOrderDate(order.getOrderDate());
                    orderDTO.setDueDate(order.getDueDate());
                    orderDTO.setStatus(order.getStatus());
                    orderDTO.setSupplierName(orderRepository.findSupplierNameByOrderId(order.getOrderId()));
                    List<String> firstItem = orderRepository.findFirstItemNameByOrder(order);
                    orderDTO.setItemName(firstItem.isEmpty() ? null : firstItem.get(0));
                    orderDTO.setItemCount(orderRepository.findOrderItemCountByOrder(order));
                    log.info("TXService: getOrderDTOS: " + orderDTO);
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    public void updateOrderStatus(String orderId, String status) {
        log.info("TXService: updateOrderStatus");
        orderRepository.updateOrderStatusById(status, orderId);
    }

    @Transactional
    public void updateOrder(OrderDTO orderDTO, List<OrderItemsDTO> orderItems) {
        log.info("TXService: updateOrder");

        String orderId = orderDTO.getOrderId();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("해당 발주 없음"));

        // 발주 정보 업데이트
        BeanUtils.copyProperties(orderDTO, order,  "orderId", "status");  // id, 상태 제외 DTO 값 복사
        order.setRealDate(new Timestamp(System.currentTimeMillis()));   // 수정 시점으로 실제등록일 변경
        order.setManager(managerRepository.findById(orderDTO.getManager()).orElse(null));
        order.setSupplier(supplierRepository.findById(orderDTO.getSupplierCode()).orElse(null));
        orderRepository.save(order);

        // 발주 품목정보 새로 저장
        orderItemsRepository.deleteByOrder(order);
        saveOrderItems(orderItems, orderId, order);
    }

    @Transactional
    public void saveSale(SaleDTO saleDTO, List<SaleItemsDTO> saleItems) {
        log.info("TXService: saveSale");
        // 수주번호 생성
        String saleId = generateNextSaleId();

        // 수주 정보 저장
        Sale sale = new Sale();
        BeanUtils.copyProperties(saleDTO, sale);      // saleDTO -> sale 필드값 복사
        sale.setSaleId(saleId);                        // 수주등록번호
        sale.setStatus("수주등록(저장)");                  // 수주상태
        sale.setRealDate(new Timestamp(System.currentTimeMillis()));
        sale.setManager(managerRepository.findById(saleDTO.getManager()).orElse(null));
        sale.setFranchise(franchiseRepository.findById(saleDTO.getFranchiseCode()).orElse(null));
        saleRepository.save(sale);

        // 발주 품목정보 저장
        saveSaleItems(saleItems, saleId, sale);
    }

    public String generateNextSaleId() {
        String maxSaleId = saleRepository.findMaxSaleId();

        String newSaleId = null;

        if (maxSaleId == null) {
            newSaleId = "SL0001";
        } else {
            int numberPart = Integer.parseInt(maxSaleId.substring(2));
            newSaleId = String.format("SL%04d", numberPart + 1);
        }
        log.info("TXService: generateNextOrderId + newOrderId = " + newSaleId);

        return newSaleId;
    }

    private void saveSaleItems(List<SaleItemsDTO> saleItems, String saleId, Sale sale) {
        for (SaleItemsDTO item : saleItems) {
            SaleItems saleItem = new SaleItems();
            BeanUtils.copyProperties(item, saleItem);
            saleItem.setItem(itemRepository.findById(item.getItemCode()).orElse(null));
            saleItem.setSale(sale);
            saleItem.setSaleItemId(saleId + item.getItemCode());
            log.info(saleItem.toString());
            saleItemsRepository.save(saleItem);
        }
    }

    public List<Franchise> findFranchises(String franchiseName) {
        if (franchiseName != null) {
            franchiseName = "%" + franchiseName + "%";
        }
        return franchiseRepository.findFranchiseOnTX(franchiseName);
    }

    public boolean checkSaleValidation(SaleDTO saleDTO) {
        String managerId = saleDTO.getManager();
        String franchiseCode = saleDTO.getFranchiseCode();
        Optional<Manager> manager = managerRepository.findById(managerId);
        Optional<Franchise> franchise = franchiseRepository.findById(franchiseCode);
        // 입력한 매니저아이디, 가맹점코드가 DB에 존재하는 값인지 검증
        return manager.isPresent() && franchise.isPresent();
    }

    public List<SaleDTO> getSaleList() {

        log.info("TXService: getOrderList");
        List<Sale> allSales = saleRepository.findAll(Sort.by(Sort.Direction.DESC, "saleId"));

        return getSaleDTOS(allSales);

    }

    public Sale getSaleById(String saleId) {
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isPresent()) {
            return sale.get();
        } else {
            throw new NoSuchElementException(saleId + "에 해당하는 수주 없음");
        }
    }

    public List<SaleItems> getSaledItems(Sale sale) {
        return saleItemsRepository.findBySale(sale);
    }

    public List<SaleDTO> searchSales(String status, String franchiseName, String orderDateStart, String orderDateEnd,
                                       String itemName, String dueDateStart, String dueDateEnd) {
        log.info("TXService: searchSales");

        // 날짜 자료형 String -> Timestamp 변경
        Timestamp orderStart = convertToTimestamp(orderDateStart);
        Timestamp orderEnd = convertToTimestamp(orderDateEnd);
        Timestamp dueStart = convertToTimestamp(dueDateStart);
        Timestamp dueEnd = convertToTimestamp(dueDateEnd);

        String formattedStatus = (status != null && !status.trim().isEmpty()) ? status : null;

        // LIKE 검색할 것들 % 붙여주기
        String formattedFranchiseName = franchiseName != null && !franchiseName.trim().isEmpty() ? "%" + franchiseName + "%" : null;
        String formattedItemName = itemName != null && !itemName.trim().isEmpty() ? "%" + itemName + "%" : null;

        log.info("status: " + formattedStatus + " supplierName: " + formattedFranchiseName + " orderDateStart: " + orderStart + " orderDateEnd: " + orderEnd + " itemName: " + formattedItemName + " dueStart: " + dueStart + " dueEnd: " + dueEnd);

        List<Sale> salesByConditions = saleRepository.findSalesByConditions
                (formattedStatus, formattedFranchiseName, orderStart, orderEnd, formattedItemName, dueStart, dueEnd);

        log.info("TXService: searchSalesByConditions" + salesByConditions);

        return getSaleDTOS(salesByConditions);
    }

    private List<SaleDTO> getSaleDTOS(List<Sale> salesByConditions) {
        return salesByConditions.stream()
                .map(sale -> {
                    SaleDTO saleDTO = new SaleDTO();
                    saleDTO.setSaleId(sale.getSaleId());
                    saleDTO.setTotalPrice(sale.getTotalPrice());
                    saleDTO.setOrderDate(sale.getOrderDate());
                    saleDTO.setDueDate(sale.getDueDate());
                    saleDTO.setStatus(sale.getStatus());
                    saleDTO.setFranchiseName(saleRepository.findFranchiseNameBySaleId(sale.getSaleId()));
                    List<String> firstItem = saleRepository.findFirstItemNameBySale(sale);
                    saleDTO.setItemName(firstItem.isEmpty() ? null : firstItem.get(0));
                    saleDTO.setItemCount(saleRepository.findSaleItemCountBySale(sale));
                    log.info("TXService: getSaleDTOS: " + saleDTO);
                    return saleDTO;
                })
                .collect(Collectors.toList());
    }

    public void updateSaleStatus(String saleId, String status) {
        log.info("TXService: updateSaleStatus");
        saleRepository.updateSaleStatusById(status, saleId);
    }

    @Transactional
    public void updateSale(SaleDTO saleDTO, List<SaleItemsDTO> saleItems) {
        log.info("TXService: updateSale");

        String saleId = saleDTO.getSaleId();
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new EntityNotFoundException("해당 수주 없음"));

        // 발주 정보 업데이트
        BeanUtils.copyProperties(saleDTO, sale,  "saleId", "status");  // id, 상태 제외 DTO 값 복사
        sale.setRealDate(new Timestamp(System.currentTimeMillis()));   // 수정 시점으로 실제등록일 변경
        sale.setManager(managerRepository.findById(saleDTO.getManager()).orElse(null));
        sale.setFranchise(franchiseRepository.findById(saleDTO.getFranchiseCode()).orElse(null));
        saleRepository.save(sale);

        // 발주 품목정보 새로 저장
        saleItemsRepository.deleteBySale(sale);
        saveSaleItems(saleItems, saleId, sale);
    }

    public List<SaleDTO> findToShip() {
        List<SaleDTO> allQualified = saleRepository.findAllQualified();
        allQualified.forEach(sale -> {
                Sale thisSale = saleRepository.findById(sale.getSaleId()).orElse(null);
                if (thisSale != null) {
                    sale.setItemCount(saleRepository.findSaleItemCountBySale(thisSale));
                    List<String> itemNames = saleRepository.findFirstItemNameBySale(thisSale);
                    if (!itemNames.isEmpty()) {
                        sale.setItemName(saleRepository.findFirstItemNameBySale(thisSale).get(0));
                    }
                }
            });
        log.info(allQualified.toString());
        return allQualified;
    }

    public List<SaleItemsDTO> getSaleItems(String saleId) {
        return saleItemsRepository.findBySale2(saleId);
    }

    public void completeShip(ShipmentDTO shipmentDTO) {
        log.info("TXService: completeShip");
        // 출하번호 생성
        String shipmentId = generateNextShipId();

        // 출하 정보 저장
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(shipmentDTO, shipment);
        log.info(shipmentDTO.toString());
        shipment.setShipmentId(shipmentId);
        shipment.setStatus("출하등록(검품요청)");
        shipment.setRealDate(new Timestamp(System.currentTimeMillis()));
        shipment.setManager(managerRepository.findById(shipmentDTO.getManager()).orElse(null));
        shipment.setSale(saleRepository.findById(shipmentDTO.getSaleId()).orElse(null));
        log.info(shipment.toString());
        shipmentRepository.save(shipment);
    }

    public String generateNextShipId() {
        String maxShipId = shipmentRepository.findMaxShipmentId();

        String newShipId = null;

        if (maxShipId == null) {
            newShipId = "SM0001";
        } else {
            int numberPart = Integer.parseInt(maxShipId.substring(2));
            newShipId = String.format("SM%04d", numberPart + 1);
        }
        return newShipId;
    }

    public List<ShipmentDTO> getShipList() {
        List<Shipment> allShips = shipmentRepository.findAll(Sort.by(Sort.Direction.DESC, "shipmentId"));
        return getShipmentDTOS(allShips);
    }

    public List<ShipmentDTO> searchShips(String status, String franchiseName, String shipDateStart, String shipDateEnd,
                                       String itemName, String dueDateStart, String dueDateEnd) {
        log.info("TXService: searchShips");

        // 날짜 자료형 String -> Timestamp 변경
        Timestamp shipStart = convertToTimestamp(shipDateStart);
        Timestamp shipEnd = convertToTimestamp(shipDateEnd);
        Timestamp dueStart = convertToTimestamp(dueDateStart);
        Timestamp dueEnd = convertToTimestamp(dueDateEnd);

        String formattedStatus = (status != null && !status.trim().isEmpty()) ? status : null;

        // LIKE 검색할 것들 % 붙여주기
        String formattedFranchiseName = franchiseName != null && !franchiseName.trim().isEmpty() ? "%" + franchiseName + "%" : null;
        String formattedItemName = itemName != null && !itemName.trim().isEmpty() ? "%" + itemName + "%" : null;

        log.info("status: " + formattedStatus + " supplierName: " + formattedFranchiseName + " shipStart: " + shipStart + " shipEnd: " + shipEnd + " itemName: " + formattedItemName + " dueStart: " + dueStart + " dueEnd: " + dueEnd);

        List<Shipment> shipmentsByConditions = shipmentRepository.findShipmentByConditions
                (formattedStatus, formattedFranchiseName, shipStart, shipEnd, formattedItemName, dueStart, dueEnd);

        log.info("TXService: shipmentsByConditions" + shipmentsByConditions);

        return getShipmentDTOS(shipmentsByConditions);
    }

    private List<ShipmentDTO> getShipmentDTOS(List<Shipment> shipmentByConditions) {
        return shipmentByConditions.stream()
                .map(ship -> {
                    ShipmentDTO shipmentDTO = new ShipmentDTO();
                    shipmentDTO.setShipmentId(ship.getShipmentId());
                    shipmentDTO.setShipDate(ship.getShipDate());
                    shipmentDTO.setDueDate(ship.getSale().getDueDate());
                    shipmentDTO.setStatus(ship.getStatus());
                    shipmentDTO.setFranchiseName(saleRepository.findFranchiseNameBySaleId(ship.getSale().getSaleId()));
                    List<String> firstItem = saleRepository.findFirstItemNameBySale(ship.getSale());
                    shipmentDTO.setItemName(firstItem.isEmpty() ? null : firstItem.get(0));
                    shipmentDTO.setItemCount(saleRepository.findSaleItemCountBySale(ship.getSale()));
                    log.info("TXService: shipmentDTO: " + shipmentDTO);
                    return shipmentDTO;
                })
                .collect(Collectors.toList());
    }

}
