package com.techtactix.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techtactix.app.filters.JwtFilter;




@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired 
	private JwtFilter jwtFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(customizer -> customizer.disable())
		.authorizeHttpRequests(request -> request
				.requestMatchers("/register", "/login", "/error").permitAll()
				// READ endpoints - accessible to USER and ADMIN (authenticated)
	            .requestMatchers(HttpMethod.GET, "/jobPosts").hasAnyRole("USER", "ADMIN")
	            .requestMatchers(HttpMethod.GET, "/jobPost/**").hasAnyRole("USER", "ADMIN")
	            .requestMatchers(HttpMethod.GET, "/jobPost/keyword/**").hasAnyRole("USER", "ADMIN")
	            // WRITE endpoints - ADMIN only
	            .requestMatchers(HttpMethod.POST, "/jobPost").hasRole("ADMIN")
	            .requestMatchers(HttpMethod.PUT, "/jobPost").hasRole("ADMIN")
	            .requestMatchers(HttpMethod.DELETE, "/jobPost/**").hasRole("ADMIN")
	            // seed/load endpoint - ADMIN only
	            .requestMatchers("/load").hasRole("ADMIN")
				//everything else authenticated
				.anyRequest().authenticated())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authProvider())
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails user=User.withDefaultPasswordEncoder()
//				.username("sj67")
//				.password("s@123")
//				.roles("USER")
//				.build();
//		
//		UserDetails admin=User.withDefaultPasswordEncoder()
//				.username("admin")
//				.password("admin890")
//				.roles("ADMIN")
//				.build();
//		
//		
//		
//		return new InMemoryUserDetailsManager(user,admin);
//	}
}
