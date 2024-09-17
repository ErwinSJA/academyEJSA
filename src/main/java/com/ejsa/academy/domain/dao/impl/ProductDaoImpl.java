package com.ejsa.academy.domain.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.ejsa.academy.domain.dao.interf.ProductDao;
import com.ejsa.academy.domain.entity.product.Product;
import com.ejsa.academy.domain.repository.specification.ProductSpecification;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.impl.product.ProductProjectionImpl;
import com.ejsa.academy.model.projection.impl.product.ReportProductsProjectionImpl;
import com.ejsa.academy.model.projection.interf.product.ProductProjection;
import com.ejsa.academy.model.projection.interf.product.ReportProductsProjection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductDaoImpl implements ProductDao{
	
	@PersistenceContext
	private EntityManager entityManager;

    @Override
    public Page<ProductProjection> findAllToPage(ProductSpecification productSpec, Pageable pageable) throws APIException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<ProductProjectionImpl> query = cb.createQuery(ProductProjectionImpl.class);
        Root<Product> productRoot = query.from(Product.class);
        
        query.select(cb.construct(ProductProjectionImpl.class,
        		productRoot.get("productId"),
        		productRoot.get("productName"),
        		productRoot.get("productPrice"),
        		productRoot.get("productStatus")))
             .where(productSpec.toPredicate(productRoot, query, cb));

        applySorting(query, cb, productRoot, pageable.getSort());

        TypedQuery<ProductProjectionImpl> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<ProductProjectionImpl> products = typedQuery.getResultList();

        List<ProductProjection> productProjections = products.stream()
            .map(p -> (ProductProjection) p)
            .toList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countProduct = countQuery.from(Product.class);
        countQuery.select(cb.count(countProduct)).where(productSpec.toPredicate(countProduct, countQuery, cb));

        Long totalRows = entityManager.createQuery(countQuery).getSingleResult();
        
        return new PageImpl<>(productProjections, pageable, totalRows);
    }
    
    private void applySorting(CriteriaQuery<ProductProjectionImpl> query, CriteriaBuilder cb, Root<Product> product, Sort sort) {
        if (sort.isSorted()) {
            sort.stream().forEach(order -> {
                if (order.isAscending()) {
                    query.orderBy(cb.asc(product.get(order.getProperty())));
                } else {
                    query.orderBy(cb.desc(product.get(order.getProperty())));
                }
            });
        }
    }
    
    public Page<ReportProductsProjection> findReportProducts(Pageable pageable){
    	StoredProcedureQuery query = entityManager.createStoredProcedureQuery("uspRptProductos");
    	query.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
    	query.execute();
    	
    	@SuppressWarnings("unchecked")
    	List<Object[]> queryResult = query.getResultList();
    	
    	List<ReportProductsProjection> resultProjection = queryResult.stream().map(row -> {
    		ReportProductsProjectionImpl projection = new ReportProductsProjectionImpl();
    		projection.setProductId(((Integer) row[0]).intValue());
    		projection.setCategoria((String) row[1]);
    		projection.setProducto((String) row[2]);
    		projection.setPrecioUnitario(((BigDecimal) row[3]).doubleValue());
    		projection.setEstado((String) row[4]);
    		return projection;
    	}).collect(Collectors.toList());    	
    	
    	 int totalRows = resultProjection.size();
         int start = (int) pageable.getOffset();
         int end = Math.min((start + pageable.getPageSize()), totalRows);
         
         return new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
    }
}
