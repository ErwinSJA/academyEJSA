package com.ejsa.academy.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejsa.academy.domain.entity.order.OrderBimonthlyView;
import com.ejsa.academy.model.projection.interf.order.OrderPendingBimonthlyViewProjection;

@Repository
public interface OrderPendingBimonthlyViewRepository extends JpaRepository<OrderBimonthlyView, Integer> {
	
	Page<OrderPendingBimonthlyViewProjection> findAllOrderPendingBimonthlyViewProjectionBy(Pageable pageable);
}
