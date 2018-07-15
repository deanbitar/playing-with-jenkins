package com.revature.database;

import java.util.ArrayList;

import com.revature.objects.DetailStyleReimb;
import com.revature.objects.ManagerStyleReimb;
import com.revature.objects.Reimbursement;
import com.revature.objects.User;

public interface Dao {
	
	//CREATE
	public int insertNewUser(User user);
	public int insertNewReimbursement(Reimbursement reimbursement);
	
	//READ
	public User getUserByUsername(String username);
	public Reimbursement getReimbursementById(int id);
	public DetailStyleReimb getDetailReimbById(int id);
	public ArrayList<Reimbursement> getReimbursementsOfAuthor(int authorId);
	public String getUserHash(String username, String password);
	public ArrayList<ManagerStyleReimb> getAllReimbs();
	
	//UPDATE
	public int updateReimbursement(Reimbursement reimbursement);
}
