package com.techtactix.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techtactix.app.model.User;


public interface UserRepo extends JpaRepository<User, Integer>{

	User findByUsername(String username);
}
