package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "quality_sale")
@Getter
@Setter
@ToString
public class QualitySale {

	@Id
	@Column(name = "quality_sale_id" , length = 20 , nullable = false)
	private String quality_sale_id;
	
	@Column(name = "order_date" , nullable = false)
	private Timestamp order_date;
	
	@Column(name = "due_date" , nullable = false)
	private Timestamp due_date;

	@Column(name = "status" , length = 20 ,nullable = false)
	private String status;
	
	@Column(name = "note" , length = 100)
	private String note;

	@OneToOne
	@JoinColumn(name = "sale_id")
	private Sale sale;
	
	@Column(name = "manager" , length = 20 ,nullable = false)
	private String manager;
	
	@OneToMany
	@JoinColumn(name = "quality_sale_id")
	private List<QualitySaleItems> quality_sale_items;

}
