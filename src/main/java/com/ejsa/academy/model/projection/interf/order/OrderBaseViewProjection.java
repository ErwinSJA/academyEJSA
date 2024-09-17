package com.ejsa.academy.model.projection.interf.order;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "ordenId","customer", "cantidad", "precioTotal", "fechaOrden", "fechaAttended"})
public interface OrderBaseViewProjection {
	Integer getOrdenId();
	String getCustomer();
	Integer getCantidad();
	Double getPrecioTotal();
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate getFechaOrden();
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate getFechaAttended();
}
