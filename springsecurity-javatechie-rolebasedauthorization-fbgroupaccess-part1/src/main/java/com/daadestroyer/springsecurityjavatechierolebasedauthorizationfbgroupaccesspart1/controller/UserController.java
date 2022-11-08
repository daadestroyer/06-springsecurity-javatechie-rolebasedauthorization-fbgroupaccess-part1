package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.constact.UserConstant;
import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity.User;
import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.repo.UserRepo;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// PUBLIC API
	@PostMapping("/join")
	public String joinGroup(@RequestBody User user) {

		// assigning default role to the new user
		user.setRole(UserConstant.DEFAULT_ROLE);

		// encrypting the password
		String encryptedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);

		this.userRepo.save(user);

		return "Hi " + user.getUserName() + " welcome to group!";
	}

	// if logged in user is ADMIN -> ADMIN or MODERATOR

	// if logged in user is MODERATOR -> MODERATOR

	// ADMIN OR MODERATOR API
	@GetMapping("/access/{userId}/{userRole}")
	// @Secured("ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String giveAccessToUser(@PathVariable int userId, @PathVariable String userRole, Principal principal) {
		User user = userRepo.findById(userId).get();
		
		List<String> activeRoles = getRolesByLoggedInUser(principal);
		
		System.out.println("activeRoles " + activeRoles);
		String newRole = "";
		System.out.println("userRole="+userRole);
		
		System.out.println(activeRoles.contains(userRole));
		
		if (activeRoles.contains(userRole)) {
			System.out.println("ENTERED....");
			
			newRole += user.getRole() + "," + userRole;
			
			System.out.println("NEW ROLE = " + newRole);
			
			user.setRole(newRole);
		}
		
		
		userRepo.save(user);
		return "Hi " + user.getUserName() + " New Role assign to you by " + principal.getName();
	}

	// getting validating logged in user and getting back that user
	private List<String> getRolesByLoggedInUser(Principal principal) {
		String roles = getLoggedInUser(principal).getRole();
		
		System.out.println("ROLES OF LOGGED IN USER = " + roles);
		
		List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
		
		
		if (assignRoles.contains("ROLE_ADMIN")) {
			return Arrays.stream(UserConstant.ADMIN_ACCESS).collect(Collectors.toList());
		}
		if (assignRoles.contains("ROLE_MODERATOR")) {
			return Arrays.stream(UserConstant.MODERATOR_ACCESS).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private User getLoggedInUser(Principal principal) {
		User user = userRepo.findByUserName(principal.getName()).get();
		System.out.println("LOGGED IN USER = " + user);
		return userRepo.findByUserName(principal.getName()).get();
	}

	// ADMIN OR MODERATOR API
	// http://localhost:8080/user/loadAllUser
	//@Secured("ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/loadAllUser")
	public List<User> getAllUser() {
		System.out.println("===FETCHED	 USER===");
		System.out.println(userRepo.findAll());
		return this.userRepo.findAll();
	}

	// USERS API (part of application)
	// localhost:8080/user/test
	@GetMapping("/test")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String testUserAccess() {
		return "user can only access this api";
	}

}
