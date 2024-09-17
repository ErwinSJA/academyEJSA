package com.ejsa.academy.model.projection.interf.order;

import java.time.LocalDate;
import java.util.List;

import com.ejsa.academy.model.projection.interf.customer.CustomerProjection;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "orderId","customer", "totalOrderAmount", "totalOrderPrice", "orderDate", "serviceOrderDate", "orderStatus",  "orderDetails" })
public interface OrderProjection {
	Integer getOrderId();
	CustomerProjection getCustomer();
    Integer getTotalOrderAmount();
    Double getTotalOrderPrice();
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate getOrderDate();
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate getServiceOrderDate();
    Integer getOrderStatus();    
    List<OrderDetailsProjection> getOrderDetails();
}
