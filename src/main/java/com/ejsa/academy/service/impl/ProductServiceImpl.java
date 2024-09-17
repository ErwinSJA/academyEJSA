package com.ejsa.academy.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ejsa.academy.domain.dao.interf.ProductDao;
import com.ejsa.academy.domain.entity.product.Product;
import com.ejsa.academy.domain.repository.ProductRepository;
import com.ejsa.academy.domain.repository.specification.ProductSpecification;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.product.ProductProjection;
import com.ejsa.academy.model.projection.interf.product.ReportProductsProjection;
import com.ejsa.academy.presentation.dto.RegisterProductDto;
import com.ejsa.academy.presentation.dto.UpdateProductDto;
import com.ejsa.academy.service.interf.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	private final ModelMapper modelMapper;
	
	@Autowired
	private ProductDao productDAO;
	
	public Page<ProductProjection> getProductsByNameAndStatus(String productName, Integer productStatus, Pageable pageable)
	{
		ProductSpecification productSpec = ProductSpecification.builder() 
				.productName(productName)
				.productStatus(productStatus)
				.build();
		
		return productDAO.findAllToPage(productSpec, pageable);
	}
	
	public ProductProjection getProductById(Integer productId)
	{
		ProductProjection product = null;
		try {
			product = productRepository.findByProductId(productId);
			if (product == null) {
				throw new APIException("Product not found", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error while searching for product", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return product;
	}
	
	
	public ProductProjection registerProduct(RegisterProductDto productDto) {
		Product productEntity = new Product();
		ProductProjection productProjection = null;
		try {
			modelMapper.map(productDto, productEntity);
			productEntity = productRepository.saveAndFlush(productEntity);
			productProjection = productRepository.findByProductId(productEntity.getProductId());
		}
		catch (Exception e) {
			throw new APIException("Error registering the product",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return productProjection;
	}
	
	public ProductProjection updateProduct(UpdateProductDto productDto) {
		Product productEntity = null;
		ProductProjection productProjection = null;
		try {			
			productEntity = productRepository.findById(productDto.getProductId())
					.orElseThrow(() -> new APIException("Product not found", HttpStatus.NOT_FOUND));
			
			modelMapper.map(productDto, productEntity);
			productEntity = productRepository.saveAndFlush(productEntity);
			productProjection = productRepository.findByProductId(productEntity.getProductId());
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error updating the product",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return productProjection;
	}
	
	public void deleteProduct(Integer productId)
	{
		try {
			var product = productRepository.findById(productId)
				.orElseThrow(() -> new APIException("Product not found", HttpStatus.NOT_FOUND));
			
			productRepository.deleteById(product.getProductId());
			productRepository.flush();
		}
		catch (APIException e) {
			throw e;
		}
		catch (DataAccessException e) {
			log.error("Error deleting product in DB",e.getMessage());
			throw new APIException("Error deleting product in DB", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			throw new APIException("Error while searching for product", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public Page<ReportProductsProjection> findReportProducts(Pageable pageable){
		Page<ReportProductsProjection> reportProjection = null;
		
		try {
			reportProjection = productDAO.findReportProducts(pageable);
			
			if (!reportProjection.hasContent()) {
				throw new APIException("No data", HttpStatus.NOT_FOUND);
			}
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			throw new APIException("Error executing the product report", HttpStatus.INTERNAL_SERVER_ERROR);
		}			
		
		return reportProjection;
	}
}
