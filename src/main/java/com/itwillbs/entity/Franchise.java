package com.itwillbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "franchise")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Franchise {

    @Id
    @Column(name = "franchise_code", length = 20, nullable = false)
    private String franchiseCode;

    @Column(name = "franchise_name", length = 100, nullable = false)
    private String franchiseName;

    @Column(name = "owner_name", length = 50, nullable = false)
    private String ownerName;

    @Column(name = "business_number", length = 20, nullable = false)
    private String businessNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "use_yn", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private char useYN = 'Y';
}
