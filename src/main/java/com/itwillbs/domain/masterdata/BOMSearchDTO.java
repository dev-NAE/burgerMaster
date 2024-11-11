package com.itwillbs.domain.masterdata;

import lombok.Data;

@Data
public class BOMSearchDTO {
	private String ppName;
	private String rmName;
	private Boolean includeUnused;

}
