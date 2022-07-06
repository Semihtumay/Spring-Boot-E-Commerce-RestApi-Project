package com.works.repositories;

import com.works.entities.Category;
import com.works.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByNameContainsIgnoreCase(@Param("name") String name);

    List<Product> findByCategory_IdEquals(Integer id);



}