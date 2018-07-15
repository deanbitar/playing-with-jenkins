package com.revature.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import com.revature.objects.DetailStyleReimb;
import com.revature.objects.ManagerStyleReimb;
import com.revature.objects.Reimbursement;
import com.revature.objects.User;
import com.revature.servlet.MasterServlet;

import oracle.net.aso.s;

public class DatabaseDao implements Dao {
	private static final String URL = "jdbc:oracle:thin:@wvudatabase.c4xtbqyyevqe.us-east-2.rds.amazonaws.com:1521:ORCL";
	private static final String USERNAME = "expenser";
	private static final String PASSWORD = "admin";

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int insertNewUser(User user) {

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String sql = "INSERT INTO USERS(USERNAME, PASSWORD, USER_FIRST_NAME, USER_LAST_NAME, USER_EMAIL, USER_ROLE_ID) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFirstName());
			statement.setString(4, user.getLastName());
			statement.setString(5, user.getEmail());
			statement.setInt(6, user.getRole());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public int insertNewReimbursement(Reimbursement reimbursement) {

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			String sql = "INSERT INTO REIMBURSEMENT(REIMB_AMOUNT, REIMB_DESCRIPTION, "
					+ "REIMB_AUTHOR, REIMB_STATUS_ID, REIMB_TYPE_ID) "
					+ "VALUES(?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setDouble(1, reimbursement.getAmount());
			statement.setString(2, reimbursement.getDescription());
			statement.setInt(3, reimbursement.getAuthor());
			statement.setInt(4, reimbursement.getStatus());
			statement.setInt(5, reimbursement.getType());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public User getUserByUsername(String username) {

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			System.out.println(statement);
			ResultSet resultSet = statement.executeQuery();
			System.out.println(resultSet.isBeforeFirst());
			if (resultSet.next()) {
				return new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Reimbursement getReimbursementById(int id) {

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			String sql = "SELECT * FROM REIMBURSEMENT WHERE REIMB_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				Reimbursement reimbursement = new Reimbursement();
				reimbursement.setId(resultSet.getInt(1));
				reimbursement.setAmount(resultSet.getDouble(2));
				reimbursement.setSubmittedTime(resultSet.getTimestamp(3));
				reimbursement.setResolvedTime(resultSet.getTimestamp(4));
				reimbursement.setDescription(resultSet.getString(5));
				reimbursement.setReciept(resultSet.getBlob(6));
				reimbursement.setAuthor(resultSet.getInt(7));
				reimbursement.setResolver(resultSet.getInt(8));
				reimbursement.setStatus(resultSet.getInt(9));
				reimbursement.setType(resultSet.getInt(10));

				return reimbursement;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<Reimbursement> getReimbursementsOfAuthor(int authorId) {

		ArrayList<Reimbursement> reimbursements = null;

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			String sql = "SELECT * FROM REIMBURSEMENT WHERE REIMB_AUTHOR = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, authorId);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.isBeforeFirst())
				reimbursements = new ArrayList<Reimbursement>();

			Reimbursement reimbursement;
			while (resultSet.next()) {
				reimbursement = new Reimbursement();
				reimbursement.setId(resultSet.getInt(1));
				reimbursement.setAmount(resultSet.getDouble(2));
				reimbursement.setSubmittedTime(resultSet.getTimestamp(3));
				reimbursement.setResolvedTime(resultSet.getTimestamp(4));
				reimbursement.setDescription(resultSet.getString(5));
				reimbursement.setReciept(resultSet.getBlob(6));
				reimbursement.setAuthor(resultSet.getInt(7));
				reimbursement.setResolver(resultSet.getInt(8));
				reimbursement.setStatus(resultSet.getInt(9));
				reimbursement.setType(resultSet.getInt(10));

				reimbursements.add(reimbursement);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reimbursements;
	}

	@Override
	public int updateReimbursement(Reimbursement reimbursement) {
		
		int result = -1;

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			
			String sql = "UPDATE REIMBURSEMENT SET REIMB_STATUS_ID = ?, "
					+ "REIMB_RESOLVER = ? "
					+ "WHERE REIMB_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, reimbursement.getStatus());
			statement.setInt(2, reimbursement.getResolver());
			statement.setInt(3, reimbursement.getId());
			
			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public String getUserHash(String username, String password) {
		String hash = null;

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			
			String sql = "{ ? = call GET_USER_HASH(?, ?)}";
			CallableStatement statement = connection.prepareCall(sql);
			statement.setString(2, username);
			statement.setString(3, password);
			
			statement.registerOutParameter(1, Types.VARCHAR);
			
			statement.execute();
			
			return statement.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hash;
	}
	
	@Override
	public ArrayList<ManagerStyleReimb> getAllReimbs(){
		
		ArrayList<ManagerStyleReimb> reimbursements = null;
		
		try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)){
			
			String sql = "SELECT REIMBURSEMENT.REIMB_ID, " + 
					"REIMBURSEMENT.REIMB_AMOUNT, " + 
					"USERS.USER_FIRST_NAME || ' ' || USERS.USER_LAST_NAME AS AUTHOR, " + 
					"REIMBURSEMENT.REIMB_SUBMITTED, " + 
					"REIMBURSEMENT.REIMB_TYPE_ID, " + 
					"REIMBURSEMENT.REIMB_STATUS_ID " + 
					"FROM REIMBURSEMENT JOIN USERS ON REIMBURSEMENT.REIMB_AUTHOR = USERS.USER_ID";
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sql);
			
			if(resultSet.isBeforeFirst())
				reimbursements = new ArrayList<ManagerStyleReimb>();
			
			ManagerStyleReimb reimbursement;
			while(resultSet.next()) {
				reimbursement = new ManagerStyleReimb();
				
				reimbursement.setReimbId(resultSet.getInt(1));
				reimbursement.setAmount(resultSet.getDouble(2));
				reimbursement.setAuthor(resultSet.getString(3));
				reimbursement.setTimeSubmitted(resultSet.getTimestamp(4));
				reimbursement.setType(resultSet.getInt(5));
				reimbursement.setStatus(resultSet.getInt(6));

				reimbursements.add(reimbursement);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return reimbursements;
	}

	@Override
	public DetailStyleReimb getDetailReimbById(int id) {
		DetailStyleReimb reimb = null;
		
		try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			
			String sql = "SELECT A.REIMB_ID, A.REIMB_AMOUNT, A.REIMB_SUBMITTED, A.REIMB_RESOLVED, " + 
					"A.REIMB_DESCRIPTION, A.REIMB_RECEIPT, B.USER_FIRST_NAME || ' ' || B.USER_LAST_NAME AS AUTHOR, " + 
					"C.USER_FIRST_NAME || ' ' || C.USER_LAST_NAME AS RESOLVER, " + 
					"A.REIMB_STATUS_ID, A.REIMB_TYPE_ID " + 
					"FROM REIMBURSEMENT A JOIN USERS B ON A.REIMB_AUTHOR = B.USER_ID " + 
					"JOIN USERS C ON A.REIMB_RESOLVER = C.USER_ID " + 
					"WHERE A.REIMB_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				reimb = new DetailStyleReimb();
				
				reimb.setId(resultSet.getInt(1));
				reimb.setAmount(resultSet.getDouble(2));
				reimb.setSubmittedTime(resultSet.getTimestamp(3));
				reimb.setResolvedTime(resultSet.getTimestamp(4));
				reimb.setDescription(resultSet.getString(5));
				reimb.setReciept(resultSet.getBlob(6));
				reimb.setAuthor(resultSet.getString(7));
				reimb.setResolver(resultSet.getString(8));
				reimb.setStatus(resultSet.getInt(9));
				reimb.setType(resultSet.getInt(10));
				
				return reimb;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return reimb;
	}
}
