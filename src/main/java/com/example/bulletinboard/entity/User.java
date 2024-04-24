package com.example.bulletinboard.entity;

import java.util.List;

import com.example.bulletinboard.security.principalUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	private int userId;
	private String email;
	private String password;	
	private String name;
	private String phone;
	
	private List<Authority> authorities;
	
	public principalUser toPrincipal() {
		return principalUser.builder()
				.userId(userId)
				.email(email)
				.password(password)
				.name(name)
				.phone(phone)
				.authorities(authorities)
				.build();
	}
}
