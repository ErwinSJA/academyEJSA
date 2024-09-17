package com.ejsa.academy.domain.entity.order;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.ejsa.academy.domain.entity.customer.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Order implements Serializable{

	private static final long serialVersionUID = 228876902738636444L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ORDER_ID")
	private Integer orderId;
	
	@ManyToOne()
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;
	
	@Column(name = "TOTAL_ORDER_AMOUNT")
	private Integer totalOrderAmount;
	
	@Column(name = "TOTAL_ORDER_PRICE")
	private Double totalOrderPrice;
	
	@Column(name = "ORDER_DATE")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate orderDate;
	
	@Column(name = "SERVICE_ORDER_DATE")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate serviceOrderDate;
	
	@Column(name = "\"status\"")
	private Integer orderStatus;
	
	@OneToMany(mappedBy = "id.order", cascade = CascadeType.ALL)
	@JsonManagedReference
    private List<OrderDetails> orderDetails;
}
