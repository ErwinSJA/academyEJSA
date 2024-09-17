package com.ejsa.academy.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.product.ProductProjection;
import com.ejsa.academy.model.projection.interf.product.ReportProductsProjection;
import com.ejsa.academy.presentation.dto.RegisterProductDto;
import com.ejsa.academy.presentation.dto.UpdateProductDto;
import com.ejsa.academy.service.interf.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("")
	public ResponseEntity<Page<ProductProjection>> getProductsByNameAndStatus(
			@RequestParam(name = "productName", required = false) String productName,
			@RequestParam(name = "status",required = false) Integer status,
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size,
			@SortDefault(sort = "productId", direction = Direction.ASC) Sort sort) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<ProductProjection> products = productService.getProductsByNameAndStatus(productName, status, pageable);
		
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductProjection> getProductById(@PathVariable Integer productId) throws APIException {
		ProductProjection product = productService.getProductById(productId);
		
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<ProductProjection> registerProduct(@RequestBody RegisterProductDto productDto) throws APIException {
		ProductProjection productCreated = productService.registerProduct(productDto);
		return new ResponseEntity<>(productCreated, HttpStatus.CREATED);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<ProductProjection> updateProduct(@RequestBody UpdateProductDto productDto) throws APIException {
		ProductProjection productUpdated = productService.updateProduct(productDto);
		return new ResponseEntity<>(productUpdated, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Integer productId) throws APIException{
		productService.deleteProduct(productId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/reportProducts")
	public ResponseEntity<Page<ReportProductsProjection>> getReportProducts(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws APIException {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<ReportProductsProjection> products = productService.findReportProducts(pageable);
		
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
}
