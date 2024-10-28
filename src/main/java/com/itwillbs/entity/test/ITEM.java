package com.itwillbs.entity.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ITEM")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ITEM {

    @Id
    @Column(name = "ITEM_CODE", length = 20)
    private String itemCode;

    @Column(name = "ITEM_NAME", length = 100, nullable = false)
    private String itemName;

    @Column(name = "ITEM_TYPE", length = 10, nullable = false)
    private String itemType;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private char useYN;
}
