package com.ejsa.academy.domain.dao.interf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ejsa.academy.model.projection.interf.order.ReportOrdersByCustomerProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersProjection;

public interface OrderDao {
	
	Page<ReportOrdersProjection> findReportOrders(String storeName, Pageable pageable);
	
	Page<ReportOrdersByCustomerProjection> findReportOrdersByCustomer(String customerName, Pageable pageable);
}
