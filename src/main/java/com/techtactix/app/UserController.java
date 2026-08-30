package com.techtactix.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techtactix.app.model.User;
import com.techtactix.app.repo.UserRepo;
import com.techtactix.app.service.JwtService;
import com.techtactix.app.service.UserService;


@RestController
public class UserController {
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserService service;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/register")
	public User registerUser(@RequestBody User user) {
		return service.saveUser(user);
	}

	@PostMapping("/login")
	public String userLogin(@RequestBody User user) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		if (authentication.isAuthenticated()) {
			// load saved user to get role
	        User dbUser = userRepo.findByUsername(user.getUsername());
	        String role = dbUser != null ? dbUser.getRole() : "USER";
	        return jwtService.getToken(user.getUsername(), role);
		} else {
			return "Login failed";
		}
	}
}



