package com.ejsa.academy.model.projection.interf.customer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "customerId", "customerName","producto", "totalOrdenes"})
public interface CustomerOrdersMonthlyViewProjection {

	Integer getCustomerId();
	String getCustomerName();
	String getProducto();
	Integer getTotalOrdenes();
}
