package com.ejsa.academy.service.interf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ejsa.academy.model.projection.interf.order.OrderPendingBimonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.OrderProjection;
import com.ejsa.academy.model.projection.interf.order.OrderRejectedMonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersByCustomerProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersProjection;

public interface OrderService {
	Page<OrderProjection> getOrders(Pageable pageable);
	
	Page<OrderRejectedMonthlyViewProjection> getMonthlyViewOrdersRejected(Pageable pageable);
	
	Page<OrderPendingBimonthlyViewProjection> getBimonthlyViewOrdersPending(Pageable pageable);
	
	Page<ReportOrdersProjection> getMonthlyReportOrdersReceived(Pageable pageable);
	
	Page<ReportOrdersProjection> getQuaterlyReportOrdersAttended(Pageable pageable);
	
	Page<ReportOrdersByCustomerProjection> getReportOrdersByCustomer(String customerName, Pageable pageable);
}
