package com.app.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.BillingDetails;
import com.app.common.constant.ApiConstant;
import com.app.entity.Book;
import com.app.entity.BooksDiscountDetails;
import com.app.model.ShoppingCartItem;
import com.app.services.BookCartService;


/**
 * Class: BooKCartController
 *
 */
@RestController 
public class BooKCartController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	BookCartService service;

	
	@PostMapping(value = "/book/save")
	public int saveBook(final @RequestBody Book book) {
		log.info("Saving book details in the database.");
		service.saveBook(book);
		return book.getId();
	}



	@GetMapping(value = ApiConstant.APPI_URL, produces = "application/json")
	public List<Book> getAllBooks() {
		
		log.info("Getting book details from the database.");
		return service.getAllBooks();
	}


	@PostMapping(value = "/discovents/save")
	public String saveDiscounts(final @RequestBody BooksDiscountDetails discountItemInfo) {
		log.info("Saving book details in the database.");
		service.saveBooksDiscountDetails(discountItemInfo);
		return "Saving DiscountDetails details in the database.";
	}


	@GetMapping(value = ApiConstant.DISCOUNT_URL, produces = "application/json")
	public List<BooksDiscountDetails> getAllBookDiscountDetails() {
		
		log.info("Getting book details from the database.");
		return service.getAllBookDiscountDetails();
	}


	@PostMapping(value = "/books/calculatePrice")
	public BillingDetails calculatePrice(final @RequestBody List<String> shoppingCartItem) {
		
		log.info("Saving book details in the database.");

		return service.calculatePrice(shoppingCartItem);
	}

}
