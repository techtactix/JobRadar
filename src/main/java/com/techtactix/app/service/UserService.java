package com.techtactix.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techtactix.app.model.User;
import com.techtactix.app.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public User saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole("USER"); // enforce USER role to prevent unauthorized role escalation
		return repo.save(user);
	}
}
