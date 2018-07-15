package com.revature.database;

import java.util.ArrayList;

import com.revature.objects.DetailStyleReimb;
import com.revature.objects.ManagerStyleReimb;
import com.revature.objects.Reimbursement;
import com.revature.objects.User;

public class DatabaseService {
	private static DatabaseDao dao = new DatabaseDao();

	public static User getUserByUserPass(String username, String password) {

		User user = dao.getUserByUsername(username);

		if (user == null) {
			System.out.println("Database returned null");
			return null;
		} else {
			String hashedPass = dao.getUserHash(username, password);
			System.out.println(hashedPass);
			System.out.println(user.getPassword());

			if (hashedPass.equals(user.getPassword()))
				return user;
			else
				return null;
		}
	}

	public static Reimbursement[] getReimbsOfAuthor(int authorId) {
		ArrayList<Reimbursement> reimbursements = dao.getReimbursementsOfAuthor(authorId);

		if (reimbursements == null)
			return null;
		else {
			System.out.println("arraylist not null");
			return reimbursements.toArray(new Reimbursement[reimbursements.size()]);
		}
	}

	public static int addNewReimb(Reimbursement reimbursement) {
		return dao.insertNewReimbursement(reimbursement);
	}

	public static ManagerStyleReimb[] getAllReimbs() {

		ArrayList<ManagerStyleReimb> reimbursements = dao.getAllReimbs();

		if (reimbursements == null)
			return null;
		else {
			return reimbursements.toArray(new ManagerStyleReimb[reimbursements.size()]);
		}
	}

	public static Reimbursement getReimbById(int reimbId) {

		Reimbursement reimbursement = dao.getReimbursementById(reimbId);

		if (reimbursement == null)
			return null;
		else
			return reimbursement;
	}

	public static DetailStyleReimb getDetailReimbById(int reimbId) {

		DetailStyleReimb reimbursement = dao.getDetailReimbById(reimbId);

		if (reimbursement == null)
			return null;
		else
			return reimbursement;
	}
	
	public static int updateReimb(Reimbursement reimbursement) {
		return dao.updateReimbursement(reimbursement);
	}
	
	public static int addNewUser(User user) {
		return dao.insertNewUser(user);
	}
}
