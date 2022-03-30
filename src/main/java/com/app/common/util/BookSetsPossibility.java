package com.app.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.entity.BooksDiscountDetails;
import com.app.model.BookSet;
import com.app.repository.BooksDiscountRepository;
import com.app.services.impl.BookCartServiceImpl;

import static java.util.Comparator.comparing;

import java.math.BigDecimal;


class BookSetsPossibility {

    private final List<BookSet> bookSets;
    
    @Autowired
    BookCartServiceImpl bookCartServiceImpl;

    BookSetsPossibility(List<String> books, int maxBookSetSize) {
        this.bookSets = generateBookSets(books, maxBookSetSize);
    }

    private List<BookSet> generateBookSets(List<String> books, int maxBookSetSize) {
        List<BookSet> bookSets = new ArrayList<>();
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

	private int getDiscount(int differentBooksCount) {
		int defaultDiscount = 0;
		List<BooksDiscountDetails> discounts = bookCartServiceImpl.getAllBookDiscountDetails();
		for (BooksDiscountDetails discount : discounts) {
			if (differentBooksCount == discount.getDifferentCopies())
				return discount.getDiscount();
		}

		return defaultDiscount;
	}


}
