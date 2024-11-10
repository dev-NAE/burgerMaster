package com.itwillbs.domain.manufacture;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MFRmListDTO {
	
	private String itemCode;
	private String itemName;
	private BigDecimal amount;
	private Integer quantity;
	
}
