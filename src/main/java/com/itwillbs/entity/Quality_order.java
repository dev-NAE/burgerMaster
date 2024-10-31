package com.itwillbs.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Quality_order")
@Getter
@Setter
@ToString
public class Quality_order {

	@Id
	@Column(name = "quality_order_id" , length = 20 , nullable = false)
	private String quality_order_id;
	
	@Column(name = "order_date" , nullable = false)
	private Timestamp order_date;
	
	
	
}
