package com.ejsa.academy.model.projection.interf.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "productId","categoria", "producto", "precioUnitario", "estado"})
public interface ReportProductsProjection {
	Integer getProductId();
	String getCategoria();
	String getProducto();
	Double getPrecioUnitario();
	String getEstado();
}
