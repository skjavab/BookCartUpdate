package com.app.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

import com.app.entity.Book;


public class BookSet extends HashSet<String>{
	
	private int discount;
	
	private HashSet<Book> books;
	
    private static final BigDecimal BOOK_PRICE = new BigDecimal(50);

    BigDecimal getPrice() {
        return new BigDecimal(this.size())
                .multiply(BOOK_PRICE)
                .multiply(new BigDecimal(getDiscount()))
                .setScale(2, RoundingMode.HALF_DOWN);
    }

    
    public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	public HashSet<Book> getBooks() {
		return books;
	}

}
