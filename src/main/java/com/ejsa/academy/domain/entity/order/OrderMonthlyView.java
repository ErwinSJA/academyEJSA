package com.ejsa.academy.domain.entity.order;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="View_OrdenesRechazadas_1Mes")
public class OrderMonthlyView extends OrderBaseView implements Serializable{

	private static final long serialVersionUID = 132807570089205897L;

}