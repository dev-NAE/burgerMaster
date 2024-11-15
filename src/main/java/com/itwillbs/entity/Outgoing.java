package com.itwillbs.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@ToString(exclude = {"manager", "outgoingItems"}) // 'manager'와 'incomingItems' 필드를 toString에서 제외
@NoArgsConstructor
@AllArgsConstructor
public class Outgoing {
	
	@Id
	@Column(name = "outgoing_id", length = 20)
	private String outgoingId;
	
	@Column(name = "outgoing_start_date")
	private Timestamp outgoingStartDate;
	
	@Column(name = "outgoing_end_date")
	private Timestamp outgoingEndDate;
	
//	@Column(name = "manager_id")
//	private String managerId;
	
	@Column(name = "status")
	private String status;
	
    //생산번호 : manufacture_order테이블의 order_id
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "production_id")
    private MFOrder mfOrder;

	@OneToOne(optional = true)
	@JoinColumn(name = "sale_id")
	private Sale sale;
 
    // Many-to-One 관계 설정: Incoming과 Manager
	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;
    
    // One-to-Many 관계 설정
    @OneToMany(mappedBy = "outgoing", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutgoingItems> outgoingItems = new ArrayList<>();
}
