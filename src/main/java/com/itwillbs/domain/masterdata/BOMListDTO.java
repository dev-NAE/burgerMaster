package com.itwillbs.domain.masterdata;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BOMListDTO {
    private Long bomId;
    private String ppCode;
    private String ppName;
    private String rmCode;
    private String rmName;
    private BigDecimal quantity;
    private String useYN;

    public BOMListDTO(Long bomId, String ppCode, String ppName, 
                      String rmCode, String rmName, 
                      BigDecimal quantity, String useYN) {
        this.bomId = bomId;
        this.ppCode = ppCode;
        this.ppName = ppName;
        this.rmCode = rmCode;
        this.rmName = rmName;
        this.quantity = quantity;
        this.useYN = useYN;
    }
}