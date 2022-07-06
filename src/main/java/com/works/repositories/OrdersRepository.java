package com.works.repositories;

import com.works.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByIdIs(Long id);

    List<Orders> findByCustomer_Id(Long id);






}