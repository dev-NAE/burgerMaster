package com.itwillbs.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 출고 테이블
 */
@Entity
@Table(name = "outgoing")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Outgoing {
	
	@Id
	@Column(name = "outgoing_id", length = 20)
	private String outgoingId;
	
	@Column(name = "outgoing_start_date")
	private Timestamp outgoingStartDate;
	
	@Column(name = "outgoing_end_date")
	private Timestamp outgoingEndDate;
	
	@Column(name = "manager_id")
	private String managerId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "production_id")
	private String productionId;

	@OneToOne
	@JoinColumn(name = "sale_id")
	private Sale sale;
 
    // Many-to-One 관계 설정: Incoming과 Manager
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    private Manager manager;
    
    // One-to-Many 관계 설정
    @OneToMany(mappedBy = "outgoing", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OutgoingItems> outgoingItems;
}
