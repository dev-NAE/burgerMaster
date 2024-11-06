package com.itwillbs.domain.manufacture;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MFRmDTO {
	
	private String itemName;
	private BigDecimal itemAmount;
	
	@Override
	public String toString() {
		return itemName+" "+itemAmount.toString();
	}
	
}
