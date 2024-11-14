package com.itwillbs.domain.quality;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QualitySaleDTO {
    private String qualitySaleId;
    private Timestamp orderDate;
    private Timestamp dueDate;
    private String status;
    private String managerName;
}
