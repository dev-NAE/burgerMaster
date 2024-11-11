package com.itwillbs.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 입고 테이블
 */
@Entity
@Table(name = "incoming")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Incoming {

	@Id
	@Column(name = "incoming_id", length = 20)
	private String incomingId;
	
	@Column(name = "incoming_start_date")
	private Timestamp incomingStartDate;
	
	@Column(name = "incoming_end_date")
	private Timestamp incomingEndDate;
	
	@Column(name = "manager_id")
	private String managerId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "production_id")
	private String productionId;
	
	@Column(name = "quality_order_id")
	private String qualityOrderId;
	

    // Many-to-One 관계 설정: Incoming과 Manager
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    private Manager manager;
    
    // One-to-Many 관계 설정
    @OneToMany(mappedBy = "incoming", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IncomingItems> incomingItems;
	
	
}
