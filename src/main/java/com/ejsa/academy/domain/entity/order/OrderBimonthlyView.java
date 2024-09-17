package com.ejsa.academy.domain.entity.order;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="View_OrdenesPendientes_2Meses")
public class OrderBimonthlyView extends OrderBaseView implements Serializable{
	
	private static final long serialVersionUID = 6200966497836006653L;
}