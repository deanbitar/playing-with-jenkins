package com.revature.servlet;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

public class RequestHelper {
	public static void handleRequest(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		if (uri.endsWith("login.do")) {
			System.out.println("Matched login.do");
			LoginController.login(request, response);
		}
		else if(uri.endsWith("getAuthorReimbs.do")) {
			ReimbsController.sendReimbsOfAuthor(request, response);
		}
		else if(uri.endsWith("addNewReimb.do")) {
			ReimbsController.addNewReimb(request, response);
		}
		else if(uri.endsWith("getAllReimbs.do")) {
			ReimbsController.sendAllReimbs(request, response);
		}
		else if(uri.endsWith("getdetailreimbbyid.do")) {
			ReimbsController.sendDetailReimbOfId(request, response);
		}
		else if(uri.endsWith("giveverdict.do")) {
			ReimbsController.updateReimb(request, response);
		}
		else if(uri.endsWith("createaccount.do")) {
			LoginController.createNewEmployee(request, response);
		}
		else {
			response.getWriter().write("action not found");
		}
	}
}
