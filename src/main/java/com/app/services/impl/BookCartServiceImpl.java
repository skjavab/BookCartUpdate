package com.app.services.impl;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.BillingDetails;
import com.app.model.BookSet;
import com.app.entity.Book;
import com.app.entity.BooksDiscountDetails;
import com.app.model.BooksSet;
import com.app.model.ShoppingCartItem;
import com.app.repository.BookRepository;
import com.app.repository.BooksDiscountRepository;
import com.app.services.BookCartService;

@Service
public class BookCartServiceImpl implements BookCartService {

	
	@Autowired
	BookRepository repository;
	
	@Autowired
	BooksDiscountRepository booksDiscountRepository;
	

	public void saveBook(final Book book) {
		repository.save(book);
	}

	public List<Book> getAllBooks() {
		
		List<Book> books = new ArrayList<>();
		
		repository.findAll().forEach(book -> books.add(book));
		return books;
	}

	@Override
	public void saveBooksDiscountDetails(BooksDiscountDetails discountItemInfo) {
		booksDiscountRepository.save(discountItemInfo);

	}

	@Override
	public List<BooksDiscountDetails> getAllBookDiscountDetails() {
		final ArrayList<BooksDiscountDetails> booksDiscountDetails = new ArrayList<BooksDiscountDetails>();
		booksDiscountRepository.findAll().forEach(discountItemInfo -> booksDiscountDetails.add(discountItemInfo));

		return booksDiscountDetails;
	}

	@Override
	public BillingDetails calculatePrice(List<String> shoppingCartItem) {
		BillingDetails billingDetails =new BillingDetails();
		List<BookSet> setsOfDifferentBooks = 
				getDifferentBooksSetsWithMaxTotalDiscount(shoppingCartItem);

		double totalPrice = 0.0;
		double setPrice = 0.0;

		for (BookSet booksSet : setsOfDifferentBooks) {
			for (Book book : booksSet.getBooks()) {
				setPrice += book.getPrice();
				
			}

			setPrice = setPrice * (1.0 - (booksSet.getDiscount() / 100.0));
			totalPrice += setPrice;
			setPrice = 0;
		}
		billingDetails.setSetsOfDifferentBooks(setsOfDifferentBooks);
		billingDetails.setTotalAmount(totalPrice+"");
		return billingDetails;
	}

	public List<BookSet> getDifferentBooksSetsWithMaxTotalDiscount(List<String> shoppingCartItems) {

		List<BookSet> optimizeSetList;

		optimizeSetList = getBestCombinationBooksSets(shoppingCartItems);
		

		return optimizeSetList;
	}

	private List<BookSet> getBestCombinationBooksSets(List<String> shoppingCartItems) {
		List<List<BookSet>> differentBooksSetsCombinations = new ArrayList<>();

			differentBooksSetsCombinations.add(generateBookSets(shoppingCartItems,shoppingCartItems.stream().distinct().count()));

		List<BookSet> optimizeSetList;

		if (differentBooksSetsCombinations.size() > 1)
			optimizeSetList = selectBooksSetsWithMaxDiscount(differentBooksSetsCombinations);
		else
			optimizeSetList = differentBooksSetsCombinations.get(0);
	
		return optimizeSetList;
	}

	/*
	 * private List<BookSet>
	 * calculateDifferentBooksSetsByMaxSize(List<ShoppingCartItem>
	 * shoppingCartItems, int maxSizeSet) { List<ShoppingCartItem>
	 * remainingShoppingCartItems = cloneShoppingCartItems(shoppingCartItems);
	 * List<BookSet> setsOfDifferentBooks = new ArrayList<>();
	 * 
	 * while (remainingShoppingCartItems.size() > 0) { final BookSet
	 * oneSetOfDifferentBooks = createNextSet(remainingShoppingCartItems,
	 * maxSizeSet); setsOfDifferentBooks.add(oneSetOfDifferentBooks); }
	 * 
	 * return setsOfDifferentBooks; }
	 */

	private BooksSet createNextSet(List<ShoppingCartItem> remainingShoppingCartItems, int maxSizeSet) {
		HashSet<Book> books = new HashSet<>();
		 Map<Integer,Book> bookMap = new HashMap<>();
		repository.findAll().forEach(book -> bookMap.put(book.getId(), book));
		for (ShoppingCartItem item : new ArrayList<>(remainingShoppingCartItems)) {
			Book book=item.getBook();
			book.setPrice(bookMap.get(book.getId()).getPrice());
			book.setTitle(bookMap.get(book.getId()).getTitle());
			books.add(book);
            
			if (item.getQuantity() == 1)
				remainingShoppingCartItems.remove(item);
			else
				item.changeQuantity(item.getQuantity() - 1);

			if (books.size() == maxSizeSet) {
				break;
			}
		}

		BooksSet booksSet = new BooksSet(books, getDiscount(books.size()));
		
		return booksSet;
	}

	/**
	 * @param booksSetsCombinations
	 * @return list of books
	 */
	private List<BookSet> selectBooksSetsWithMaxDiscount(List<List<BookSet>> booksSetsCombinations) {
		List<BookSet> maxDiscountBooksSets = null;
		int maxBooksSetsDiscount = 0;
		int totalBooksSetsDiscount = 0;

		for (List<BookSet> booksSets : booksSetsCombinations) {
			for (BookSet booksSet : booksSets) {
				totalBooksSetsDiscount += booksSet.getDiscount();
			}

			if (maxBooksSetsDiscount < totalBooksSetsDiscount) {
				maxDiscountBooksSets = booksSets;
				maxBooksSetsDiscount = totalBooksSetsDiscount;
			}

			totalBooksSetsDiscount = 0;
		}

		return maxDiscountBooksSets;
	}

	private List<ShoppingCartItem> cloneShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
		List<ShoppingCartItem> shoppingCartItemsCopy = new ArrayList<>();

		for (ShoppingCartItem item : shoppingCartItems) {
			shoppingCartItemsCopy.add(new ShoppingCartItem(item.getBook(), item.getQuantity()));
		}

		return shoppingCartItemsCopy;
	}

	private int getDiscount(int differentBooksCount) {
		int defaultDiscount = 0;
		List<BooksDiscountDetails> discounts = getAllBookDiscountDetails();
		for (BooksDiscountDetails discount : discounts) {
			if (differentBooksCount == discount.getDifferentCopies())
				return discount.getDiscount();
		}

		return defaultDiscount;
	}
	
    private List<BookSet> generateBookSets(List<String> books, long maxBookSetSize) {
        List<BookSet> bookSets = new ArrayList<BookSet>();
        for (String book : books) {
            Optional<BookSet> biggestAvailableBookSet = bookSets.stream()
                    .filter(bookSet -> bookSet.size() < maxBookSetSize && !bookSet.contains(book))
                    .max(comparing(BookSet::size));

            if (biggestAvailableBookSet.isPresent()) {
                biggestAvailableBookSet.get().add(book);
            } else {
                BookSet bookSet = new BookSet();
                bookSet.add(book);
                bookSets.add(bookSet);
                bookSet.setDiscount(getDiscount(bookSet.size()));
                
            }
        }
        return bookSets;
    }
}
