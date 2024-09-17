package com.ejsa.academy.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejsa.academy.domain.entity.product.Product;
import com.ejsa.academy.model.projection.interf.product.ProductProjection;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	ProductProjection findByProductId(Integer productId);
}
