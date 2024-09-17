package com.ejsa.academy.domain.entity.product;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PRODUCTS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Product implements Serializable{
	
	private static final long serialVersionUID = -6155995904074167465L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PRODUCT_ID")
	private Integer productId;
	
	@Column(name = "CATEGORY_ID")
	private Integer categoryId;
	
	@NotBlank
	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Column(name = "PRODUCT_PRICE")
	private Double productPrice;
	
	@Column(name = "\"status\"")
	private Integer productStatus;
}
