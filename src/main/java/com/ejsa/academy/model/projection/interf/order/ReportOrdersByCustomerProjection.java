package com.ejsa.academy.model.projection.interf.order;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "ordenId","customer", "producto", "cantidad", "fechaOrden", "fechaAttended"})
public interface ReportOrdersByCustomerProjection {
	Integer getOrdenId();
	String getProducto();
	Integer getCantidad();
	String getCustomer();
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate getFechaOrden();
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate getFechaAttended();
}
