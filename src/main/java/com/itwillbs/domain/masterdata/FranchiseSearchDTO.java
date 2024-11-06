package com.itwillbs.domain.masterdata;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FranchiseSearchDTO {
	private String franchiseName;
	private String ownerName;
	private String businessNumber;
	private LocalDate contractStartDate;
	private LocalDate contractEndDate;
	private Boolean includeUnused = false;
}
