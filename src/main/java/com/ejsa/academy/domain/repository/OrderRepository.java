package com.ejsa.academy.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ejsa.academy.domain.entity.order.Order;
import com.ejsa.academy.model.projection.interf.order.OrderProjection;

public interface OrderRepository extends JpaRepository<Order,Integer> {
	
	Page<OrderProjection> findAllBy(Pageable pageable);
}
