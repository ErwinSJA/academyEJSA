package com.ejsa.academy.domain.entity.order;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class OrderBaseView implements Serializable{

	private static final long serialVersionUID = -4892383081647112068L;
	
	@Id
	@Column(name="ORDENID")
	private Integer ordenId;

	@Column(name = "CUSTOMER")
	private String customer;
	
	@Column(name = "CANTIDAD")
	private Integer cantidad;
	
	@Column(name = "PRECIOTOTAL")
	private Double precioTotal;
	
	@Column(name = "FECHAORDEN")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechaOrden;
	
	@Column(name = "FECHAATTENDED")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechaAttended;
}
