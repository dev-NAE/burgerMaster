package com.itwillbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "franchise")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Franchise {

	@Id
	@Column(name = "franchise_code", length = 20, nullable = false)
	@NotBlank(message = "가맹점코드는 필수입니다.")
	@Pattern(regexp = "^FR\\d{3}$", message = "코드패턴 : FR000")
	private String franchiseCode;

	@Column(name = "franchise_name", length = 100, nullable = false)
	@NotBlank(message = "가맹점명은 필수입니다.")
	@Length(min = 2, max = 100, message = "가맹점명은 2~100자 사이여야 합니다.")
	private String franchiseName;

	@Column(name = "owner_name", length = 50, nullable = false)
	@NotBlank(message = "점주명은 필수입니다.")
	@Pattern(regexp = "^[가-힣A-Za-z\\s]+$", message = "점주명에 허용되지 않는 문자가 포함되어 있습니다.")
	@Length(max = 50, message = "점주명은 50자 이하여야 합니다.")
	private String ownerName;

	@Column(name = "business_number", length = 20, nullable = false)
	@NotBlank(message = "사업자번호는 필수입니다.")
	@Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자번호 패턴 : 000-00-00000")
	private String businessNumber;

	@Column(name = "email", length = 100)
	@Pattern(regexp = "^$|^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "이메일 형식이 올바르지 않습니다.")
	@Length(max = 100, message = "이메일은 100자 이하여야 합니다.")
	private String email;

	@Column(name = "address", length = 200, nullable = false)
	@NotBlank(message = "주소는 필수입니다.")
	@Length(max = 200, message = "주소는 200자 이하여야 합니다.")
	private String address;

	@Column(name = "contract_start_date", nullable = false)
	@NotNull(message = "계약시작일은 필수입니다.")
	private LocalDate contractStartDate;

	@Column(name = "contract_end_date")
	private LocalDate contractEndDate;

	@Column(name = "use_yn", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	@Pattern(regexp = "^[YN]$", message = "useYN 값은 'Y' 또는 'N'이어야 합니다.")
	private String useYN = "Y";

}
