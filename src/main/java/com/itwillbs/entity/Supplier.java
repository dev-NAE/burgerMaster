package com.itwillbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String supplierCode;

    @Column(name = "supplier_name", length = 100, nullable = false)
    private String supplierName;

    @Column(name = "business_number", length = 20, nullable = false)
    private String businessNumber;

    @Column(name = "contact_person", length = 50, nullable = false)
    private String contactPerson;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "use_yn", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private char useYN;
}
