package com.ejsa.academy.model.projection.interf.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "productId","productName", "productPrice","productStatus"})
public interface ProductProjection {
	Integer getProductId();
	String getProductName();
	Double getProductPrice();
	Integer getProductStatus();
}
