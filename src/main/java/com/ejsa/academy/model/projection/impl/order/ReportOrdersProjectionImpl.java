package com.ejsa.academy.model.projection.impl.order;

import java.time.LocalDate;

import com.ejsa.academy.model.projection.interf.order.ReportOrdersProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportOrdersProjectionImpl implements ReportOrdersProjection{
	private Integer ordenId;
    private String customer;
    private Integer cantidad;
    private Double precioTotal;
    private LocalDate fechaOrden;
    private LocalDate fechaAttended;
    private String estado;
}
