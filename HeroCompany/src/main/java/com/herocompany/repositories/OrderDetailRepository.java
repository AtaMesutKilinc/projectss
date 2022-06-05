package com.herocompany.repositories;

import com.herocompany.entities.OrderDetail;
import com.herocompany.entities.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {
}