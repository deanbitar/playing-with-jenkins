package com.revature.objects;

import java.sql.Timestamp;

public class ManagerStyleReimb {
	int reimbId;
	double amount;
	String author;
	Timestamp timeSubmitted;
	int type;
	int status;
	
	public ManagerStyleReimb() {
	}

	public int getReimbId() {
		return reimbId;
	}

	public void setReimbId(int reimbId) {
		this.reimbId = reimbId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Timestamp getTimeSubmitted() {
		return timeSubmitted;
	}

	public void setTimeSubmitted(Timestamp timeSubmitted) {
		this.timeSubmitted = timeSubmitted;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ManagerReimb [reimbId=" + reimbId + ", amount=" + amount + ", author=" + author + ", timeSubmitted="
				+ timeSubmitted + ", type=" + type + ", status=" + status + "]";
	}
}
