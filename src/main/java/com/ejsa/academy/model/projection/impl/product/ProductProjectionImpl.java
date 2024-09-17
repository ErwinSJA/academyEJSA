package com.ejsa.academy.model.projection.impl.product;

import com.ejsa.academy.model.projection.interf.product.ProductProjection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductProjectionImpl implements ProductProjection {
	private Integer productId;
    private String productName;
    private Double productPrice;
    private Integer productStatus;
}
