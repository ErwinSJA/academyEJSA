package com.ejsa.academy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateCustomerDto {

	private Integer customerId;
    private String customerAddress;
    private String customerEmail;
}
