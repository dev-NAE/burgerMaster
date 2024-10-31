package com.itwillbs.entity.dashboard;


import java.sql.Timestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Sale")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Sale {
	
	
	@Id
	 @Column(name = "sale_id")
	private String saleId;
	 
	 @Column(name = "total_price", nullable = false)
	 private int totalPrice; 
	 
	 @Column(name = "order_date", nullable = false)
	 private Timestamp orderDate; 
	 
	 
	 @Column(name = "status", length = 20, nullable = false)
	 private String status; 
	 
	 @Column(name = "due_date",nullable = false)
	 private Timestamp dueDate; 
	

}
