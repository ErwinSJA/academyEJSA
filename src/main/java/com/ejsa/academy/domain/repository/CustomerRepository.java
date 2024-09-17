package com.ejsa.academy.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ejsa.academy.domain.entity.customer.Customer;
import com.ejsa.academy.model.projection.interf.customer.CustomerProjection;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Page<CustomerProjection> findAllProjectedBy(Pageable pageable);
	
	CustomerProjection findByCustomerId(Integer customerId);
}
