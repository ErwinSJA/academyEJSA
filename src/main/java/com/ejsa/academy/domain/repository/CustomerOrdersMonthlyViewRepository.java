package com.ejsa.academy.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejsa.academy.domain.entity.customer.CustomerOrdersMonthlyView;
import com.ejsa.academy.model.projection.interf.customer.CustomerOrdersMonthlyViewProjection;

@Repository
public interface CustomerOrdersMonthlyViewRepository extends JpaRepository<CustomerOrdersMonthlyView, Integer> {
	
	Page<CustomerOrdersMonthlyViewProjection> findAllCustomerOrdersMonthlyViewProjectionBy(Pageable pageable);
}
