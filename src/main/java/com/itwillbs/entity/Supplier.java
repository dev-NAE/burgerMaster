package com.itwillbs.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Supplier {

	@Id
	@Column(name = "supplier_code", length = 20, nullable = false)
	@NotBlank(message = "거래처코드는 필수입니다.")
	@Pattern(regexp = "^SUP\\d{3}$", message = "코드패턴 : SUP000")
	private String supplierCode;

	@Column(name = "supplier_name", length = 100, nullable = false)
	@NotBlank(message = "거래처명은 필수입니다.")
	@Length(min = 2, max = 100, message = "거래처명은 2~100자 사이여야 합니다.")
	private String supplierName;

	@Column(name = "business_number", length = 20, nullable = false)
	@NotBlank(message = "사업자번호는 필수입니다.")
	@Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자번호 패턴 : 000-00-00000")
	private String businessNumber;

	@Column(name = "contact_person", length = 50, nullable = false)
	@NotBlank(message = "담당자명은 필수입니다.")
    @Pattern(regexp = "^[가-힣A-Za-z\\s]+$", message = "담당자명에 허용되지 않는 문자가 포함되어 있습니다.")
	private String contactPerson;

	// null 허용, 기본 이메일 유효성(특수문자 허용) but 파라미터 바인딩 injection 방어
	@Column(name = "email", length = 100, nullable = true)
	@Pattern(regexp = "^$|^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "기본 이메일 검증 정규식을 만족하지 않습니다.")
	private String email;

	@Column(name = "address", length = 200, nullable = false)
	@Length(max = 200, message = "주소는 200자 이하여야 합니다.")
	@NotBlank(message = "주소는 필수입니다.")
	private String address;

	@Column(name = "use_yn", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	@Pattern(regexp = "^[YN]$", message = "useYN 값은 'Y' 또는 'N'이어야 합니다.")
	private String useYN = "Y";
}
