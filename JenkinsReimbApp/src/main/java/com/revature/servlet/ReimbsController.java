package com.revature.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.database.DatabaseService;
import com.revature.objects.DetailStyleReimb;
import com.revature.objects.ManagerStyleReimb;
import com.revature.objects.Reimbursement;

public class ReimbsController {
	public static void sendReimbsOfAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String author = request.getParameter("authorid");
		int authorId = Integer.parseInt(author);

		System.out.println(authorId);
		
		Reimbursement[] reimbursements = DatabaseService.getReimbsOfAuthor(authorId);

		response.setContentType("application/json");

		if (reimbursements != null) {
			String reimbsJson = new ObjectMapper().writeValueAsString(reimbursements);
			response.getWriter().write(reimbsJson);
		}
		else
			response.getWriter().write("{\"status\":\"no-reimbs-found\"}");
	}
	
	public static void addNewReimb(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int authorId = Integer.parseInt(request.getParameter("authorid"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		int type = Integer.parseInt(request.getParameter("type"));
		String desc = request.getParameter("desc");
		
		Reimbursement reimbursement = new Reimbursement();
		reimbursement.setAmount(amount);
		reimbursement.setAuthor(authorId);
		reimbursement.setDescription(desc);
		reimbursement.setStatus(0);
		reimbursement.setType(type);
		
		int result = DatabaseService.addNewReimb(reimbursement);
		
		response.setContentType("application/json");
		
		if(result == 1) {
			response.getWriter().write("{\"status\" : \"ok\"}");
		}
		else {
			response.getWriter().write("{\"status\" : \"bad\"}");
		}
	}
	
	public static void sendAllReimbs(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ManagerStyleReimb[] reimbursements = DatabaseService.getAllReimbs();
		
		if(reimbursements == null)
			response.getWriter().write("{\"status\":\"no-reimbs-found\"}");
		else
			response.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));
	}
	
	public static void sendReimbOfId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int reimbId = Integer.parseInt(request.getParameter("reimbid"));
		
		Reimbursement reimbursement = DatabaseService.getReimbById(reimbId);
		
		if (reimbursement == null)
			response.getWriter().write("{\"status\":\"bad-response\"}");
		else
			response.getWriter().write(new ObjectMapper().writeValueAsString(reimbursement));
	}
	
	public static void sendDetailReimbOfId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int reimbId = Integer.parseInt(request.getParameter("reimbid"));
		
		DetailStyleReimb reimbursement = DatabaseService.getDetailReimbById(reimbId);
		
		if (reimbursement == null)
			response.getWriter().write("{\"status\":\"bad-response\"}");
		else
			response.getWriter().write(new ObjectMapper().writeValueAsString(reimbursement));
	}
	
	public static void updateReimb(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int reimbId = Integer.parseInt(request.getParameter("reimbid"));
		int resolver = Integer.parseInt(request.getParameter("resolver"));
		int status = Integer.parseInt(request.getParameter("status"));
		
		Reimbursement reimbursement = DatabaseService.getReimbById(reimbId);
		reimbursement.setResolver(resolver);
		reimbursement.setStatus(status);
		
		int result = DatabaseService.updateReimb(reimbursement);
		
		if(result == 1)
			response.getWriter().write("{\"status\" : \"ok\"}");
		else
			response.getWriter().write("{\"status\" : \"bad\"}");
	}
}
