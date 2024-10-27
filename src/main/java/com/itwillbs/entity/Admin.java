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
	@Column(name = "adminID", length= 50)
	private String adminID;
	
	@Column(name = "pass", nullable = false)
	private String pass;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private Timestamp email;

	@Column(name = "phone")
	private Timestamp phone;

	@Column(name = "authority")
	private Timestamp authority;
}
