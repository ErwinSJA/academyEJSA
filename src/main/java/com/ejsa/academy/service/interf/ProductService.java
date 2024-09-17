package com.ejsa.academy.service.interf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ejsa.academy.model.projection.interf.product.ProductProjection;
import com.ejsa.academy.model.projection.interf.product.ReportProductsProjection;
import com.ejsa.academy.presentation.dto.RegisterProductDto;
import com.ejsa.academy.presentation.dto.UpdateProductDto;

public interface ProductService {
	
	Page<ProductProjection> getProductsByNameAndStatus(String productName, Integer productStatus, Pageable pageable);
	
	ProductProjection getProductById(Integer productId);
	
	ProductProjection registerProduct(RegisterProductDto productDto);
	
	ProductProjection updateProduct(UpdateProductDto productDto);
	
	void deleteProduct(Integer productId);
	
	Page<ReportProductsProjection> findReportProducts(Pageable pageable);
}
