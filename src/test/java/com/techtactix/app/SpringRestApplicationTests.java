package com.techtactix.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringRestApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	void testRegisterAndLogin() throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		
		// 1. Test Register
		String uniqueUser = "testuser_" + System.currentTimeMillis();
		HttpRequest registerReq = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/register"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"" + uniqueUser + "\",\"password\":\"test123\"}"))
				.build();

		HttpResponse<String> registerRes = client.send(registerReq, HttpResponse.BodyHandlers.ofString());
		System.out.println("REGISTER STATUS: " + registerRes.statusCode());
		System.out.println("REGISTER BODY: " + registerRes.body());
		assertEquals(200, registerRes.statusCode());

		// 2. Test Login
		HttpRequest loginReq = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/login"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"" + uniqueUser + "\",\"password\":\"test123\"}"))
				.build();

		HttpResponse<String> loginRes = client.send(loginReq, HttpResponse.BodyHandlers.ofString());
		System.out.println("LOGIN STATUS: " + loginRes.statusCode());
		System.out.println("LOGIN BODY (JWT): " + loginRes.body());
		assertEquals(200, loginRes.statusCode());
	}

	@Autowired
	private com.techtactix.app.repo.UserRepo userRepo;

	@Autowired
	private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

	@Test
	void testAdminLogin() throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest loginReq = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/login"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"shrenikadmin\",\"password\":\"admin123\"}"))
				.build();

		HttpResponse<String> loginRes = client.send(loginReq, HttpResponse.BodyHandlers.ofString());
		System.out.println("ADMIN LOGIN STATUS: " + loginRes.statusCode());
		System.out.println("ADMIN LOGIN BODY: " + loginRes.body());
		assertEquals(200, loginRes.statusCode());
	}

}
