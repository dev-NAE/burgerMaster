package com.itwillbs.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "quality_order")
@Getter
@Setter
@ToString
public class QualityOrder {

	@Id
	@Column(name = "quality_order_id" , length = 20 , nullable = false)
	private String quality_order_id;
	
	@Column(name = "order_date" , nullable = false)
	private Timestamp order_date;
	
	@Column(name = "due_date" , nullable = false)
	private Timestamp due_date;
	
	@Column(name = "stock_date" , nullable = false)
	private Timestamp stock_date;
	
	@Column(name = "status" , length = 20 ,nullable = false)
	private String status;
	
	@Column(name = "note" , length = 100)
	private String note;
	
	@Column(name = "order_id" , length = 20 ,nullable = false)
	private String order_id;
	
	@Column(name = "manager" , length = 20 ,nullable = false)
	private String manager;
	
	@OneToMany
	@JoinColumn(name = "quality_order_id")
	private List<QualityOrderItems> quality_order_items;
	

}
