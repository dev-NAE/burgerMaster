package com.itwillbs.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name="admins")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
	@Id
	@Column(name = "adminID", length= 50)
	private String adminID;
	
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

	public Admin createAdmin(Admin admin, PasswordEncoder passwordEncoder) {
		return Admin.builder()
				.adminID(admin.getAdminID())
				.pass(passwordEncoder.encode(admin.getPass()))
				.name(admin.getName())
				.email(admin.getEmail())
				.phone(admin.getPhone())
				.authority(admin.getAuthority())
				.build();
	}
}
