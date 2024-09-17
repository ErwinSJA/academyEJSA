package com.ejsa.academy.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.customer.CustomerOrdersMonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.customer.CustomerProjection;
import com.ejsa.academy.presentation.dto.RegisterCustomerDto;
import com.ejsa.academy.presentation.dto.UpdateCustomerDto;
import com.ejsa.academy.service.interf.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("")
	public ResponseEntity<Page<CustomerProjection>> getCustomersByPage(
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page, 
			@RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
			@SortDefault(sort = "customerId", direction = Direction.ASC) Sort sort) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<CustomerProjection> customers = customerService.getAllCustomersByPage(pageable);
		
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<CustomerProjection> registerCustomer(@RequestBody RegisterCustomerDto customerDto) throws APIException {
		CustomerProjection customerCreated = customerService.registerCustomer(customerDto);
		return new ResponseEntity<>(customerCreated, HttpStatus.CREATED);
	}
	
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerProjection> getCustomerById(@PathVariable("customerId") Integer customerId) throws APIException {
		CustomerProjection customer = customerService.getCustomerById(customerId);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<CustomerProjection> updateCustomer(@RequestBody UpdateCustomerDto customerDto) throws APIException {
		CustomerProjection customerUpdated = customerService.updateCustomer(customerDto);
		return new ResponseEntity<>(customerUpdated, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) throws APIException {
		customerService.deleteCustomer(customerId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/view/orders")
	public ResponseEntity<Page<CustomerOrdersMonthlyViewProjection>> getMonthlyViewCustomerOrders(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws APIException {
		
		Sort sort = Sort.by(
			    Sort.Order.desc("customerName"),
			    Sort.Order.desc("totalOrdenes"));
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<CustomerOrdersMonthlyViewProjection> orders = customerService.getMonthlyViewCustomerOrders(pageable);
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
}
