package com.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.app.entity.BooksDiscountDetails;

public interface BooksDiscountRepository extends CrudRepository<BooksDiscountDetails, Integer>{

}
