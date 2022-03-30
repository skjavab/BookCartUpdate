package com.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="book_discount_details")
public class BooksDiscountDetails implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "different_copies")
	private int differentCopies;
	
	private int discount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDifferentCopies(int differentCopies) {
		this.differentCopies = differentCopies;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getDifferentCopies() {
		return differentCopies;
	}

	public int getDiscount() {
		return discount;
	}
}
