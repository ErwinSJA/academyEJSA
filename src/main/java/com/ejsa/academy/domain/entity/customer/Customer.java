package com.ejsa.academy.domain.entity.customer;

import java.io.Serializable;
import java.util.List;

import com.ejsa.academy.domain.entity.order.Order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CUSTOMERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Customer implements Serializable{
	
	private static final long serialVersionUID = -6142206671500721879L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CUSTOMER_ID")
	private Integer customerId;
	
	@NotBlank
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "CUSTOMER_ADDRESS")
	private String customerAddress;
	
	@Column(name = "DOCUMENT_TYPE")
	private Integer documentType;
	
	@NotBlank
	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;
	
	@Email
	@Column(name = "CUSTOMER_EMAIL")
	private String customerEmail;
	
	@OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Order> orders;
}
