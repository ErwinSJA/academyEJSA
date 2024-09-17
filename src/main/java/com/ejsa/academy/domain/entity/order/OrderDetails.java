package com.ejsa.academy.domain.entity.order;

import java.io.Serializable;

import com.ejsa.academy.domain.entity.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ORDER_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderDetails implements Serializable{

	private static final long serialVersionUID = -8208328094759098684L;

	@EmbeddedId
    private OrderDetailsId id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "total_price")
    private Double totalPrice;
    
    @ManyToOne()
    @MapsId("order")
    @JoinColumn(name = "order_id", referencedColumnName = "ORDER_ID")
    @JsonBackReference
    private Order order;

    @ManyToOne()
    @MapsId("product")
    @JoinColumn(name = "product_id", referencedColumnName = "PRODUCT_ID")
    private Product product;

}
