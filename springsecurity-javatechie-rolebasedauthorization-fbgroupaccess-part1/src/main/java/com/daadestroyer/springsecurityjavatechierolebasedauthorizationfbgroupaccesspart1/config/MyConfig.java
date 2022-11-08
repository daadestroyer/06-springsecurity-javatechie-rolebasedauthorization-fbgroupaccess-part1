package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MyConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	
	// AUTHORIZATION
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			
			.authorizeRequests()
				.antMatchers("/user/join") // this is public API allowing access to all the user
				.permitAll()
			.and()
				.authorizeRequests()
				.antMatchers("/user/**") // these API need roles (roles based authorization) need MORERATOR or ADMIN rights
				.authenticated()
				.and()
				.formLogin();
		
	}
	// AUTHENTICATION
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
