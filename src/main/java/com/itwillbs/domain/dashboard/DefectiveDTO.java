package com.itwillbs.domain.dashboard;


import lombok.Data;

@Data
public class DefectiveDTO {
	private int quantity;
    private String itemName;
    private String status;
    private String note;
    private String defectiveId;
    private String itemCode;

    public DefectiveDTO(int quantity, String itemName,String status,String note,String defectiveId,String itemCode) {
        this.quantity = quantity;
        this.itemName = itemName;
        this.status = status;
        this.note = note;
        this.defectiveId = defectiveId;
        this.itemCode = itemCode;
    }
}
