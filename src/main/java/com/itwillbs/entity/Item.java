package com.itwillbs.entity;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Item {

	@Id
	@Column(name = "item_code", length = 20)
	@NotBlank(message = "품목코드는 필수입니다.")
    @Pattern(regexp = "^(RM|PP|FP)\\d{3}$", message = "품목코드는 RM, PP, FP로 시작하고 3자리 숫자여야 합니다.")
	private String itemCode;

	@NotBlank(message = "품목명은 필수입니다")
	@Length(min = 2, max = 100, message = "품목명은 2~100자 사이여야 합니다")
	@Column(name = "item_name", length = 100, nullable = false)
	private String itemName;

	@NotBlank(message = "품목유형은 필수입니다")
	@Column(name = "item_type", length = 10, nullable = false)
	private String itemType;

	@Column(name = "use_yn", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	@Pattern(regexp = "^[YN]$", message = "useYN 값은 'Y' 또는 'N'이어야 합니다.")
	private String useYN = "Y";
	
	
	
//	  InventoryItem과 일대일 관계 설정 소연
//	삭제 x 
    @OneToOne(mappedBy = "item")
    @JsonIgnore //순환 참조 강수
    private InventoryItem inventoryItem;

}
