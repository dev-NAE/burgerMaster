package com.itwillbs.service;

import com.itwillbs.domain.transaction.OrderDTO;
import com.itwillbs.domain.transaction.OrderItemsDTO;
import com.itwillbs.entity.Order;
import com.itwillbs.entity.OrderItems;
import com.itwillbs.repository.OrderItemsRepository;
import com.itwillbs.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TXService {

    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;

    public void saveOrder(OrderDTO orderDTO, List<OrderItemsDTO> orderItems) {

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
            orderItem.setOrderId(orderId);
            orderItem.setOrderItemId(orderId + item.getItem_code());
            orderItemsRepository.save(orderItem);
        }
    }

    public String generateNextOrderId() {
        String maxOrderId = orderRepository.findMaxOrderId();

        String newOrderId;
        if (maxOrderId == null) {
            newOrderId = "OD0001";
        } else {
            int numberPart = Integer.parseInt(maxOrderId.substring(2));
            newOrderId = String.format("OD%04d", numberPart + 1);
        }

        return newOrderId;
    }


}
