package com.itwillbs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BOM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bom_id")
    private Long bomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pp_code", referencedColumnName = "item_code")
    private Item processedProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rm_code", referencedColumnName = "item_code")
    private Item rawMaterial;

    @Column(name = "quantity", precision = 10, scale = 3, nullable = false)
    @Positive(message = "수량은 0보다 커야 합니다")
    private BigDecimal quantity;

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private String useYN = "Y";
}