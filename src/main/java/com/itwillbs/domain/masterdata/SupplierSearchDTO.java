package com.itwillbs.domain.masterdata;

import lombok.Data;

@Data
public class SupplierSearchDTO {
	private String supplierName;
	private String businessNumber;
	private String contactPerson;
	private Boolean includeUnused = false;
}
