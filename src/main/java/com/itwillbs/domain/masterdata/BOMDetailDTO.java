package com.itwillbs.domain.masterdata;

import com.itwillbs.entity.BOM;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
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
    
    public String getPpCode() {
        return processedProduct.getItemCode();
    }

    public String getRmCode() {
        return rawMaterial.getItemCode();
    }
}