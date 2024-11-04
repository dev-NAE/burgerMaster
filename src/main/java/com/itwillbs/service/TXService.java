package com.itwillbs.service;

import com.itwillbs.domain.transaction.OrderDTO;
import com.itwillbs.domain.transaction.OrderItemsDTO;
import com.itwillbs.entity.*;
import com.itwillbs.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    @Transactional
    public void saveOrder(OrderDTO orderDTO, List<OrderItemsDTO> orderItems) {
        log.info("TXService: saveOrder");
        // 발주번호 생성
        String orderId = generateNextOrderId();

        // 발주 정보 저장
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);      // orderDTO -> order 필드값 복사
        order.setOrderId(orderId);                        // 발주등록번호
        order.setRealDate(new Timestamp(System.currentTimeMillis()));   // 실제등록일
        order.setStatus("발주등록(저장)");                                // 발주상태
        orderRepository.save(order);

        // 발주 품목정보 저장
        for (OrderItemsDTO item : orderItems) {
            OrderItems orderItem = new OrderItems();
            BeanUtils.copyProperties(item, orderItem);
            orderItem.setOrder(order);
            orderItem.setOrderItemId(orderId + item.getItemCode());
            log.info(orderItem.toString());
            orderItemsRepository.save(orderItem);
        }
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

    public String getManagerName(String managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        if (manager.isPresent()) {
            return manager.get().getManagerId();
        } else {
            throw new NoSuchElementException(managerId + "에 해당하는 매니저 없음");
        }
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public List<Item> getOrderItems() {
        return itemRepository.findAll();
        // 사용중인 코드 + 구매할 수 있는 아이템 한정, 재고수량 오름차순으로 수정 필요
    }

    public List<OrderDTO> getOrderList() {

        log.info("TXService: getOrderList");
        List<Order> allOrders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderId"));

        return allOrders.stream().map (order -> {
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

            return orderDTO;
        })
        .collect(Collectors.toList());

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

    public List<Item> getSaleItems() {
        return itemRepository.findAll();
        // 사용중인 코드 + 판매할 수 있는 아이템 한정, 재고수량 오름차순으로 수정 필요
    }

}
