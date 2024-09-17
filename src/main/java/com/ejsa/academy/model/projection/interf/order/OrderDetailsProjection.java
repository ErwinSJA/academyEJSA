package com.ejsa.academy.model.projection.interf.order;

import com.ejsa.academy.model.projection.interf.product.ProductProjection;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "amount","totalPrice", "product"})
public interface OrderDetailsProjection {
	ProductProjection getProduct();
	Integer getAmount();
    Double getTotalPrice();    
}
