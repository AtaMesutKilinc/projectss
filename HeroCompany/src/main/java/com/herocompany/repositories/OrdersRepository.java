package com.herocompany.repositories;

import com.herocompany.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByCustomer_IdEquals(Long id);
    List<Orders> findByIdIs(Long id);





}