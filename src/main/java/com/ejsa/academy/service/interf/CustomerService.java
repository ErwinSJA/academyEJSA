package com.ejsa.academy.service.interf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ejsa.academy.model.projection.interf.customer.CustomerOrdersMonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.customer.CustomerProjection;
import com.ejsa.academy.presentation.dto.RegisterCustomerDto;
import com.ejsa.academy.presentation.dto.UpdateCustomerDto;

public interface CustomerService {
	
	Page<CustomerProjection> getAllCustomersByPage(Pageable pageable);
	
	CustomerProjection getCustomerById(Integer customerId);
	
	CustomerProjection registerCustomer(RegisterCustomerDto customerDto);
	
	CustomerProjection updateCustomer(UpdateCustomerDto customerDto);
	
	void deleteCustomer (Integer customerId);
	
	Page<CustomerOrdersMonthlyViewProjection> getMonthlyViewCustomerOrders(Pageable pageable);
}
