package com.itwillbs.domain.masterdata;

import lombok.Data;

@Data
public class ItemSearchDTO {
	private String itemName;
	private String itemType;
	private Boolean includeUnused;
}
