package com.itwillbs.domain.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvenSearchDTO {
	
    private String itemCodeOrName;
    private String reasonOfIncoming;
    private String incomingStartDate; // 문자열로 받습니다.
    private String incomingId;
    private String prodOrQualId;
    private String status;
    private String managerCodeOrName;
}
