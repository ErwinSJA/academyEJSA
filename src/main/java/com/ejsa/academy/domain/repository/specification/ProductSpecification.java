package com.ejsa.academy.domain.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.ejsa.academy.domain.entity.product.Product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductSpecification implements Specification<Product> {

	private static final long serialVersionUID = 7319314321309140850L;
	
	private String productName;
	private Integer productStatus;

	@Override
	public Predicate toPredicate(Root<Product> productRoot, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        
        if (StringUtils.hasText(productName)) {
        	predicates.add(criteriaBuilder.like( criteriaBuilder.lower(productRoot.get("productName")), 
        											"%" + productName.toLowerCase() + "%"));
        }
        
        if (productStatus != null) {
        	predicates.add(criteriaBuilder.equal(productRoot.get("productStatus"), productStatus));
        }
        
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
