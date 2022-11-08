package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class test {
	public static void main(String[] args) {
		String DEFAULT_ROLE = "ROLE_USER";
		String ADMIN_ACCESS =  "ROLE_ADMIN,ROLE_MODERATOR" ;
	
		List<String> list = Arrays.stream(ADMIN_ACCESS.split(",")).collect(Collectors.toList());
		System.out.println(list);
		
		if(list.contains("ROLE_ADMIN")) {
			System.out.println("allow");
		}
		
	}
}
