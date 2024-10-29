package com.itwillbs.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name="managers")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
	@Id
	@Column(name = "manager_id", length= 50)
	private String managerId;
	
	@Column(name = "pass", nullable = false)
	private String pass;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "authority")
	private String authority;



	public Manager createManger(Manager admin, PasswordEncoder passwordEncoder) {
		return Manager.builder()
				.managerId(admin.getManagerId())
				.pass(passwordEncoder.encode(admin.getPass()))
				.name(admin.getName())
				.email(admin.getEmail())
				.phone(admin.getPhone())
				.authority(admin.getAuthority())
				.build();
	}
}
