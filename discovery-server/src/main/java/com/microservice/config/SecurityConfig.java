package com.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{

	@Value("${eureka.username}")
	String username;
	
	@Value("${eureka.password}")
	String password;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.requestMatchers("/eureka/**").permitAll()
			.anyRequest().authenticated()
			
			.and()
			.httpBasic();
		
		return http.build();
	}
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User
				.withUsername(username)
				.password("{noop}"+password)
				.authorities("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
