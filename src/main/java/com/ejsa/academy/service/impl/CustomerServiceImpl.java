package com.ejsa.academy.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ejsa.academy.domain.entity.customer.Customer;
import com.ejsa.academy.domain.repository.CustomerOrdersMonthlyViewRepository;
import com.ejsa.academy.domain.repository.CustomerRepository;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.customer.CustomerOrdersMonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.customer.CustomerProjection;
import com.ejsa.academy.presentation.dto.RegisterCustomerDto;
import com.ejsa.academy.presentation.dto.UpdateCustomerDto;
import com.ejsa.academy.service.interf.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerOrdersMonthlyViewRepository customerOrdersMonthlyViewRepository;
	
	private final ModelMapper modelMapper;
	
	@Override
	public Page<CustomerProjection> getAllCustomersByPage(Pageable pageable) {
		return customerRepository.findAllProjectedBy(pageable);
	}
	
	public CustomerProjection getCustomerById(Integer customerId){
		CustomerProjection customer = null;
		try {
			customer = customerRepository.findByCustomerId(customerId);
			if (customer == null) {
				throw new APIException("No customer found", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error while searching for customer", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return customer;
	}
	
	
	public CustomerProjection registerCustomer(RegisterCustomerDto customerDto) {
		Customer customerEntity = new Customer();
		CustomerProjection customerProjection = null;
		try {
			modelMapper.map(customerDto, customerEntity);
			customerEntity = customerRepository.saveAndFlush(customerEntity);
			customerProjection = customerRepository.findByCustomerId(customerEntity.getCustomerId());
		}
		catch (Exception e) {
			throw new APIException("Error registering the customer",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return customerProjection;
	}
	
	public CustomerProjection updateCustomer(UpdateCustomerDto customerDto) {
		Customer customerEntity = null;
		CustomerProjection customerProjection = null;
		try {			
			customerEntity = customerRepository.findById(customerDto.getCustomerId())
					.orElseThrow(() -> new APIException("Customer not found", HttpStatus.NOT_FOUND));
			
			modelMapper.map(customerDto, customerEntity);
			customerEntity = customerRepository.saveAndFlush(customerEntity);
			customerProjection = customerRepository.findByCustomerId(customerEntity.getCustomerId());
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error updating the customer",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return customerProjection;
	}
	
	public void deleteCustomer(Integer customerId) {
		try {			
			var customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new APIException("Customer not found", HttpStatus.NOT_FOUND));

			customerRepository.deleteById(customer.getCustomerId());
			customerRepository.flush();
		}
		catch (APIException e) {
			throw e;
		}
		catch (DataAccessException e) {
			log.error("Error deleting customer in DB",e.getMessage());
			throw new APIException("Error deleting customer in DB", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			throw new APIException("Error deleting the customer",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Page<CustomerOrdersMonthlyViewProjection> getMonthlyViewCustomerOrders(Pageable pageable) {
		
		Page<CustomerOrdersMonthlyViewProjection> orderView = null;
		
		try {
			orderView = customerOrdersMonthlyViewRepository.findAllCustomerOrdersMonthlyViewProjectionBy(pageable);
			
			if (!orderView.hasContent()) {
				throw new APIException("No data in view", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error executing the Monthly Customer Orders View", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return orderView;
	}
}
