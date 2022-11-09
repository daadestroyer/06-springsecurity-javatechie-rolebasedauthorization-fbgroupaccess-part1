package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
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
public class Post {

	@Id
	@GeneratedValue
	private int postId;
	private String subject;
	private String description;
	private String userName;

	@Enumerated(EnumType.STRING)
	private PostStatus postStatus;

}
