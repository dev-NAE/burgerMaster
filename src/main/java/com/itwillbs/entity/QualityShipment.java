package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "quality_shipment")
@Getter
@Setter
public class QualityShipment {

	@Id
	@Column(name = "quality_shipment_id" , length = 20 , nullable = false)
	private String qualityShipmentId;
	
	@Column(name = "ship_date" , nullable = false)
	private Timestamp shipDate;

	@Column(name = "status" , length = 20 ,nullable = false)
	private String status;
	
	@Column(name = "note" , length = 100)
	private String note;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipment_id")
	private Shipment shipment;

	@ManyToOne
	@JoinColumn(name = "manager")
	private Manager manager;

	@OneToOne
	@JoinColumn(name = "sale_id")
	private Sale sale;

}
