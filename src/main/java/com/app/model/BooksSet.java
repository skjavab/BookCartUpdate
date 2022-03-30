
package com.app.model;

import java.util.HashSet;

import com.app.entity.Book;

public class BooksSet {
	
	private HashSet<Book> books;
	
	private int discount;

	public BooksSet(HashSet<Book> books, int discount) {
		this.books = books;
		this.discount = discount;
	}

	public HashSet<Book> getBooks() {
		return books;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

}
