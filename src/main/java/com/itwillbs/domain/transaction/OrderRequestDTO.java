package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class OrderRequestDTO {

    private OrderDTO order;
    private List<OrderItemsDTO> items;

}
