package com.ejsa.academy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateProductDto {
	
	private Integer productId;
	
	private Integer categoryId;
	
    private String productName;
    private Double productPrice;
    
    private Integer productStatus;
}
