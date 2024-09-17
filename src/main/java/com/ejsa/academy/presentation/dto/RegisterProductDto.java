package com.ejsa.academy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterProductDto {
	
	@Builder.Default
	private Integer categoryId = 7;
	
    private String productName;
    private Double productPrice;
    @Builder.Default
    private Integer productStatus = 1;
}
