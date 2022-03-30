package com.app.services;

import java.util.List;

import com.app.model.BillingDetails;
import com.app.entity.Book;
import com.app.entity.BooksDiscountDetails;
import com.app.model.ShoppingCartItem;

/**
 * interface: BookCartService
 *
 */
public interface BookCartService {

	public void saveBook(final Book book);

	public List<Book> getAllBooks();

	public void saveBooksDiscountDetails(BooksDiscountDetails discountItemInfo);

	public List<BooksDiscountDetails> getAllBookDiscountDetails();

	public BillingDetails calculatePrice(List<String> shoppingCartItem);
}
