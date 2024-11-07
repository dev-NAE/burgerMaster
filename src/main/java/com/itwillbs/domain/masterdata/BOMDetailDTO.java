package com.itwillbs.domain.masterdata;

import java.math.BigDecimal;

import com.itwillbs.entity.BOM;

import lombok.Data;

@Data
public class BOMDetailDTO {
	private Long bomId;
	private ItemDTO processedProduct;
	private ItemDTO rawMaterial;
	private BigDecimal quantity;
	private String useYN;

	public BOMDetailDTO(BOM bom) {
		this.bomId = bom.getBomId();
		this.processedProduct = new ItemDTO(bom.getProcessedProduct());
		this.rawMaterial = new ItemDTO(bom.getRawMaterial());
		this.quantity = bom.getQuantity();
		this.useYN = bom.getUseYN();
	}
}
