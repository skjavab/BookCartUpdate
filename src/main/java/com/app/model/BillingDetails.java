package com.app.model;

import java.util.List;

public class BillingDetails {
	
	String totalAmount = null;
	
	List<BookSet> setsOfDifferentBooks = null;

	public String getTotalAmount() {

		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<BookSet> getSetsOfDifferentBooks() {
		return setsOfDifferentBooks;
	}

	public void setSetsOfDifferentBooks(List<BookSet> setsOfDifferentBooks) {
		this.setsOfDifferentBooks = setsOfDifferentBooks;
	}

}
