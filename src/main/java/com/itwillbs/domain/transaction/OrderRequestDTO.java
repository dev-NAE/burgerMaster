package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderRequestDTO {

    private OrderDTO order;
    private List<OrderItemsDTO> items;

}
