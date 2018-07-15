package com.revature.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.database.DatabaseService;
import com.revature.objects.User;

public class LoginController {

	public static void login(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = DatabaseService.getUserByUserPass(username, password);
		
		System.out.println("username: " + username);
		System.out.println("password: " + password);
		
		if(user == null) {
			response.getWriter().write("{\"status\":\"bad-login\"}");
		}
		else {
			response.setContentType("application/json");
			String userJson = new ObjectMapper().writeValueAsString(user);
			response.getWriter().write(userJson);
		}
	}
	
	public static void createNewEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username, password, firstname, lastname, email;
		
		username = request.getParameter("username");
		password = request.getParameter("password");
		email = request.getParameter("email");
		firstname = request.getParameter("fname");
		lastname = request.getParameter("lname");
		
		User user = new User();
		
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		user.setRole(1);
		
		int result = DatabaseService.addNewUser(user);
		
		if(result == 1)
			response.getWriter().write("{\"status\" : \"ok\"}");
		else
			response.getWriter().write("{\"status\" : \"bad\"}");
	}
}
