package com.itwillbs.domain.manufacture;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MFBomDTO {
	
	private String itemCode;
	private String itemName;
	private String rmList;
	
}
