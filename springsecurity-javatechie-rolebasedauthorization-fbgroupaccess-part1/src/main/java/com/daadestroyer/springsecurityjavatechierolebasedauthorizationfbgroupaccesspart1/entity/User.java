package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

	@Id
	@GeneratedValue
	private int userId;
	private String username;
	private String password;
	private boolean isActive;
	private String role;// ROLE_USER,ROLE_ADMIN,ROLE_MODERATOR
}
