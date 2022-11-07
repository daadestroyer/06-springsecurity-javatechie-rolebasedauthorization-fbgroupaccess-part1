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

		return "Hi " + user.getUsername() + " welcome to group!";
	}

	// if logged in user is ADMIN -> ADMIN or MODERATOR

	// if logged in user is MODERATOR -> MODERATOR

	// ADMIN OR MODERATOR API
	@GetMapping("/access/{userId}/{userRole}")
	// @Secured("ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String changeUserAccess(@PathVariable int userId, @PathVariable String userRole, Principal principal) {
		// get the user to whom we want to give access
		User user = this.userRepo.findById(userId).get();

		// getting roles of logged in user
		List<String> rolesOfLoggedInUser = this.getRolesByLoggedInUser(principal);
		String newRole = "";
		if (rolesOfLoggedInUser.contains(userRole)) {
			newRole += user.getRole() + "," + userRole;
			user.setRole(newRole);
		}
		this.userRepo.save(user);

		return "Hi " + user.getUsername() + " new role assign to you by " + principal.getName();
	}

	// getting validating logged in user and getting back that user
	private User getLoggedInUser(Principal principal) {
		return this.userRepo.findByUserName(principal.getName()).get();
	}

	private List<String> getRolesByLoggedInUser(Principal principal) {
		// getting logged in user role string
		String roles = this.getLoggedInUser(principal).getRole();

		List<String> loggedInUserAssignedRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());

		if (loggedInUserAssignedRoles.contains("ROLE_ADMIN")) {
			return Arrays.stream(UserConstant.ADMIN_ACCESS).collect(Collectors.toList());
		} else if (loggedInUserAssignedRoles.contains("ROLE_MODERATOR")) {
			return Arrays.stream(UserConstant.MODERATOR_ACCESS).collect(Collectors.toList());
		}

		return Collections.emptyList();
	}

	// ADMIN OR MODERATOR API
	@GetMapping
	@Secured("ROLE_ADMIN")
	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<User> getAllUser() {
		return this.userRepo.findAll();
	}

	// USERS API (part of application)
	@GetMapping("/test")
	@Secured("ROLE_USER")
	public String testUserAccess() {
		return "user can only access this api";
	}
}
