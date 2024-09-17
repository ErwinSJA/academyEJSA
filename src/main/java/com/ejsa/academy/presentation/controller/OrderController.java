package com.ejsa.academy.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.order.OrderPendingBimonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.OrderProjection;
import com.ejsa.academy.model.projection.interf.order.OrderRejectedMonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersByCustomerProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersProjection;
import com.ejsa.academy.service.interf.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("")
	public ResponseEntity<Page<OrderProjection>> getOrders(
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page, 
			@RequestParam(name = "size", defaultValue = "5", required = false) Integer size) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC,"orderId"));
		
		Page<OrderProjection> orders = orderService.getOrders(pageable);
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/view/rejected/monthly")
	public ResponseEntity<Page<OrderRejectedMonthlyViewProjection>> getMonthlyViewOrdersRejected(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC,"ordenId"));
		
		Page<OrderRejectedMonthlyViewProjection> orderView = orderService.getMonthlyViewOrdersRejected(pageable);
		
		return new ResponseEntity<>(orderView, HttpStatus.OK);
	}
	
	@GetMapping("/view/pending/bimonthly")
	public ResponseEntity<Page<OrderPendingBimonthlyViewProjection>> getBimonthlyViewOrdersPending(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC,"ordenId"));
		
		Page<OrderPendingBimonthlyViewProjection> orderView = orderService.getBimonthlyViewOrdersPending(pageable);
		
		return new ResponseEntity<>(orderView, HttpStatus.OK);
	}
	
	@GetMapping("/report/received/monthly")
	public ResponseEntity<Page<ReportOrdersProjection>> getMonthlyReportOrdersReceived(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<ReportOrdersProjection> orders = orderService.getMonthlyReportOrdersReceived(pageable);
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/report/attended/quaterly")
	public ResponseEntity<Page<ReportOrdersProjection>> getQuaterlyReportOrdersAttended(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<ReportOrdersProjection> orders = orderService.getQuaterlyReportOrdersAttended(pageable);
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/report/all/monthly/byCustomer")
	public ResponseEntity<Page<ReportOrdersByCustomerProjection>> getReportOrdersByCustomer(
			@RequestParam(name = "customerName") String customerName, 
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<ReportOrdersByCustomerProjection> orders = orderService.getReportOrdersByCustomer(customerName, pageable);
		
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	
}
