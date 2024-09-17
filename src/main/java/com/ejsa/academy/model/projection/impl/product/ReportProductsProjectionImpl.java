package com.ejsa.academy.model.projection.impl.product;

import com.ejsa.academy.model.projection.interf.product.ReportProductsProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportProductsProjectionImpl implements ReportProductsProjection  {
	private Integer productId;
	private String categoria;
	private String producto;
	private Double precioUnitario;
	private String estado;
}
