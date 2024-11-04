package com.itwillbs.domain.dashboard;


import lombok.Data;

@Data
public class DefectiveDTO {
	private int quantity;
    private String itemName;
    private String status;

    public DefectiveDTO(int quantity, String itemName,String status) {
        this.quantity = quantity;
        this.itemName = itemName;
        this.status = status;
    }
}
