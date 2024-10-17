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
@Table(name="members")
@Getter
@Setter
@ToString
public class Admin {
	@Id
	@Column(name = "id", length= 50)
	private String id;
	
	@Column(name = "pass", nullable = false)
	private String pass;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "date")
	private Timestamp date;
}
