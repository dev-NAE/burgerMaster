package com.itwillbs.entity;

import org.hibernate.validator.constraints.Length;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
	private String itemCode;

	@NotBlank(message = "품목명은 필수입니다")
	@Length(min = 2, max = 100, message = "품목명은 2~100자 사이여야 합니다")
	@Column(name = "item_name", length = 100, nullable = false)
	private String itemName;

	@NotBlank(message = "품목유형은 필수입니다")
	@Column(name = "item_type", length = 10, nullable = false)
	private String itemType;

	@Column(name = "use_yn", nullable = false)
	private char useYN = 'Y';
	
	
	
	 // InventoryItem과 일대일 관계 설정 소연
	//삭제 x 
    @OneToOne(mappedBy = "item")
    private InventoryItem inventoryItem; 

}
