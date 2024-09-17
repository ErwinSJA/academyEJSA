package com.ejsa.academy.model.projection.impl.order;

import java.time.LocalDate;

import com.ejsa.academy.model.projection.interf.order.ReportOrdersByCustomerProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportOrdersByCustomerProjectionImpl implements ReportOrdersByCustomerProjection{
	private Integer ordenId;
	private String producto;
	private Integer cantidad;
	private String customer;
	private LocalDate fechaOrden;
	private LocalDate fechaAttended;
	
}
