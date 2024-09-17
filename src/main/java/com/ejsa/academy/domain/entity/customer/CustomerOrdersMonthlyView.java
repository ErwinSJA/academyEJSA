package com.ejsa.academy.domain.entity.customer;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="View_CustomerTotalOrders_30D")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerOrdersMonthlyView implements Serializable {

	private static final long serialVersionUID = 7463784521468689716L;

	@Id
	@Column(name="CUSTOMERID")
	private Integer customerId;
	
	@Column(name="TOTALORDENES")
	private Integer totalOrdenes;
	
	@Column(name="PRODUCTO")
	private String producto;
	
	@Column(name="CUSTOMER")
	private String customerName;
}
