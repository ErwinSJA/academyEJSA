package com.ejsa.academy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterCustomerDto {

	private String customerName;
	
    private String customerAddress;
    private Integer documentType;
    private String documentNumber;
    @Builder.Default
    private String customerEmail = "";
}
