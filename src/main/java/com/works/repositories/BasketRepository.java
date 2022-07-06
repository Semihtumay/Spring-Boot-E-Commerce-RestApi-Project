package com.works.repositories;

import com.works.entities.Basket;
import com.works.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
   // List<Basket> findByCustomer_EmailIsIgnoreCaseAndStatusFalse(String email);

    List<Basket> findByCreatedByEqualsAndStatusFalse(String createdBy);

    List<Basket> findByCreatedByEqualsIgnoreCase(String createdBy);





   // List<Basket> findByCustomer_Orders_IdIs(Long id);







}