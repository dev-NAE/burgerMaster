package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SaleRequestDTO {

    private SaleDTO sale;
    private List<SaleItemsDTO> items;

}
