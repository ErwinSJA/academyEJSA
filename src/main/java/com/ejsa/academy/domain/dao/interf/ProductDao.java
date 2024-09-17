package com.ejsa.academy.domain.dao.interf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ejsa.academy.domain.repository.specification.ProductSpecification;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.product.ProductProjection;
import com.ejsa.academy.model.projection.interf.product.ReportProductsProjection;

public interface ProductDao {
	
	Page<ProductProjection> findAllToPage(ProductSpecification productSpec, Pageable pageable) throws APIException;
	
	Page<ReportProductsProjection> findReportProducts(Pageable pageable) throws APIException;
}
