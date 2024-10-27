package com.itwillbs.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bom")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BOM {

    @Id
    @Column(name = "pp_code", length = 20, nullable = false)
    private String ppCode;

    @Column(name = "quantity", precision = 10, scale = 3, nullable = false)
    private BigDecimal quantity;

    @Column(name = "use_yn", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private char useYN;

    @Column(name = "rm_code", length = 20, nullable = false)
    private String rmCode;
}
