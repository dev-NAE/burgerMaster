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

	// ROLE_INBENTORY,ROLE_QUALITY 형태로 저장
	@Column(name = "manager_role")
	private String managerRole;

	public static Manager createManger(Manager manager, PasswordEncoder passwordEncoder) {
		return Manager.builder()
				.managerId(manager.getManagerId())
				.pass(passwordEncoder.encode(manager.getPass()))
				.name(manager.getName())
				.email(manager.getEmail())
				.phone(manager.getPhone())
				.managerRole(manager.getManagerRole())
				.build();
	}
}
