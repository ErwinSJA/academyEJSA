package com.ejsa.academy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ejsa.academy.domain.dao.interf.OrderDao;
import com.ejsa.academy.domain.repository.OrderRepository;
import com.ejsa.academy.domain.repository.OrderPendingBimonthlyViewRepository;
import com.ejsa.academy.domain.repository.OrderRejectedMonthlyViewRepository;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.order.OrderPendingBimonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.OrderProjection;
import com.ejsa.academy.model.projection.interf.order.OrderRejectedMonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersByCustomerProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersProjection;
import com.ejsa.academy.service.interf.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderRejectedMonthlyViewRepository orderRejectedMonthlyViewRepository;
	
	@Autowired
	private OrderPendingBimonthlyViewRepository orderPendingBimonthlyViewRepository;
	
	@Autowired
	private OrderDao orderDao;
	
	
	public Page<OrderProjection> getOrders(Pageable pageable) {
		return orderRepository.findAllBy(pageable);
	}
	
	public Page<OrderRejectedMonthlyViewProjection> getMonthlyViewOrdersRejected(Pageable pageable) {
		
		Page<OrderRejectedMonthlyViewProjection> orderView = null;
		
		try {
			orderView = orderRejectedMonthlyViewRepository.findAllOrderRejectedMonthlyViewProjectionBy(pageable);
			
			if (!orderView.hasContent()) {
				throw new APIException("No data in view", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error executing the Monthly Rejected View", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return orderView;
	}
	
	public Page<OrderPendingBimonthlyViewProjection> getBimonthlyViewOrdersPending(Pageable pageable) {
		
		Page<OrderPendingBimonthlyViewProjection> orderView = null;
		
		try {
			orderView = orderPendingBimonthlyViewRepository.findAllOrderPendingBimonthlyViewProjectionBy(pageable);
			
			if (!orderView.hasContent()) {
				throw new APIException("No data in view", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error executing the Bimonthly Pending View", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return orderView;
	}
	
	@Transactional
	public Page<ReportOrdersProjection> getMonthlyReportOrdersReceived(Pageable pageable){
		
		Page<ReportOrdersProjection> reportProjection = null;
		try {
			reportProjection = orderDao.findReportOrders("uspRptOrdenesRecibidasMensual", pageable);
			
			if (!reportProjection.hasContent()) {
				throw new APIException("No data", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error executing the monthly report", HttpStatus.INTERNAL_SERVER_ERROR);
		}			
		
		return reportProjection;
	}
	
	@Transactional
	public Page<ReportOrdersProjection> getQuaterlyReportOrdersAttended(Pageable pageable){
		
		Page<ReportOrdersProjection> reportProjection = null;
		try {
			reportProjection = orderDao.findReportOrders("uspRptOrdenesAtendidas3Meses", pageable);
			
			if (!reportProjection.hasContent()) {
				throw new APIException("No data", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error executing the quaterly report", HttpStatus.INTERNAL_SERVER_ERROR);
		}			
		
		return reportProjection;
	}
	
	@Transactional
	public Page<ReportOrdersByCustomerProjection> getReportOrdersByCustomer(String customerName, Pageable pageable){
		
		Page<ReportOrdersByCustomerProjection> reportProjection = null;
		try {
			reportProjection = orderDao.findReportOrdersByCustomer(customerName, pageable);
			
			if (!reportProjection.hasContent()) {
				throw new APIException("No data", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error executing the report orders by customer", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
		return reportProjection;
	}
}
