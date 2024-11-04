package com.itwillbs.entity.dashboard;



import java.sql.Timestamp;

import com.itwillbs.entity.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "defective")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DefectiveDash {
	
	 @Id
	 @Column(name = "defective_id", length = 20, nullable = false)
	private String defectiveId;
	 
	 @Column(name = "quantity", nullable = false)
	 private int quantity; 
	 
	 @Column(name = "defective_date", nullable = false)
	 private Timestamp defectiveDate; 
	 
	 @ManyToOne
	 @JoinColumn(name = "item_code", referencedColumnName = "item_code")
	 private Item itemCode;
	 
	 @Column(name = "status", length = 20, nullable = false)
	 private String status; 
	 
	 @Column(name = "note",length = 100, nullable = false)
	 private String note; 
	
	
	
	
	
	
	

}//
