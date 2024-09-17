package com.ejsa.academy.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejsa.academy.domain.entity.order.OrderMonthlyView;
import com.ejsa.academy.model.projection.interf.order.OrderRejectedMonthlyViewProjection;

@Repository
public interface OrderRejectedMonthlyViewRepository extends JpaRepository<OrderMonthlyView, Integer> {
	
	Page<OrderRejectedMonthlyViewProjection> findAllOrderRejectedMonthlyViewProjectionBy(Pageable pageable);
}
