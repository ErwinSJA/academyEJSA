package com.ejsa.academy.model.projection.interf.customer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "customerId","customerName", "documentType","documentNumber","customerEmail"})
public interface CustomerProjection {
	Integer getCustomerId();
	String getCustomerName();
	Integer getDocumentType();
	String getDocumentNumber();
	String getCustomerEmail();
}
