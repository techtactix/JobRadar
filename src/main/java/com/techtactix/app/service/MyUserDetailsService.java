package com.techtactix.app.service;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techtactix.app.model.User;
import com.techtactix.app.model.UserPrincipal;
import com.techtactix.app.repo.UserRepo;



@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=repo.findByUsername(username);
		if(user == null) {
			System.out.println("User not found");
			throw new UsernameNotFoundException("User 404");
		}
		
		
		return new UserPrincipal(user);
	}

	
}
