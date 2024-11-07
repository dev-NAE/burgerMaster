package com.itwillbs.domain.masterdata;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BOMListDTO {
    private String ppCode;
    private String ppName;
    private String rmCode;
    private String rmName;
    private BigDecimal quantity;
    private String useYN;

    public BOMListDTO(String ppCode, String ppName, String rmCode, 
                     String rmName, BigDecimal quantity, String useYN) {
        this.ppCode = ppCode;
        this.ppName = ppName;
        this.rmCode = rmCode;
        this.rmName = rmName;
        this.quantity = quantity;
        this.useYN = useYN;
    }
}
