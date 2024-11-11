package com.itwillbs.domain.masterdata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BOMSaveDTO {
    @NotBlank(message = "가공품 코드는 필수입니다")
    private String ppCode;

    @NotBlank(message = "원재료 코드는 필수입니다")
    private String rmCode;

    @Positive(message = "수량은 0보다 커야 합니다")
    private BigDecimal quantity;

    private String useYN = "Y";
}