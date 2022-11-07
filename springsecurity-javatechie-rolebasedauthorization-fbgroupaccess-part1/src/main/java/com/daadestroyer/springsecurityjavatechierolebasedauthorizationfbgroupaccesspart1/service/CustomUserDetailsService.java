package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity.CustomUserDetails;
import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity.User;
import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.repo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// we fetched the User object from DB
		Optional<User> user = this.userRepo.findByUserName(username);
		 
		System.out.println("User in DB = "+user);
		// but we need to return UserDetails object and not User object
		return user.map(CustomUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException(username + " Not Found"));
	}

}
