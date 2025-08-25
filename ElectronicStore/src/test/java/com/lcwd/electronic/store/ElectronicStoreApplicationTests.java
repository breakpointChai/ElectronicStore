package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {


	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtHelper jwtHelper;


	@Test
	void contextLoads() {
	}

	@Test
	void testToken(){

		System.out.println("Testing jwt token");

		User user = userRepository.findByEmail("pradeep@gmail.com").get();

		String token= jwtHelper.generateToken(user);
		System.out.println(token);

		System.out.println("getting username from token");

		System.out.println(jwtHelper.getUsernameFromToken(token));

		System.out.println("Token expired: ");
		System.out.println(jwtHelper.isTokenExpired(token));
	}
}
