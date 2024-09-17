package com.ejsa.academy.domain.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ejsa.academy.domain.dao.interf.OrderDao;
import com.ejsa.academy.model.projection.impl.order.ReportOrdersByCustomerProjectionImpl;
import com.ejsa.academy.model.projection.impl.order.ReportOrdersProjectionImpl;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersByCustomerProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersProjection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class OrderDaoImpl implements OrderDao{
	
	@Autowired
    private EntityManager entityManager;
	
	@Override
	public Page<ReportOrdersProjection> findReportOrders(String storeName, Pageable pageable) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery(storeName);

        query.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
        query.execute();

        @SuppressWarnings("unchecked")
		List<ReportOrdersProjection> resultProjection = mapResultsToProjection(query.getResultList());
        
        int totalRows = resultProjection.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalRows);

        return new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	}
	
	private List<ReportOrdersProjection> mapResultsToProjection(List<Object[]> results){
		
		return results.stream().map(row -> {
        	ReportOrdersProjectionImpl projection = new ReportOrdersProjectionImpl();
        	projection.setOrdenId(((Integer) row[0]).intValue());
        	projection.setCustomer((String) row[1]);
        	projection.setCantidad(((Integer) row[2]).intValue());
        	projection.setPrecioTotal(((BigDecimal) row[3]).doubleValue());
        	projection.setFechaOrden(((Timestamp) row[4]).toLocalDateTime().toLocalDate());
        	projection.setFechaAttended(((Timestamp) row[5]) == null ? null: ((Timestamp) row[5]).toLocalDateTime().toLocalDate());
        	projection.setEstado(row.length>6 ? ((String) row[6]) : null);
			return projection;
		}).collect(Collectors.toList());
	}

	@Override
	public Page<ReportOrdersByCustomerProjection> findReportOrdersByCustomer(String customerName, Pageable pageable) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("uspRptOrdenProductoMensualByCliente");

        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
        query.setParameter(1, customerName);
        
        query.execute();
        
        @SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
        
		List<ReportOrdersByCustomerProjection> resultProjection = resultList.stream().map(row -> {
			ReportOrdersByCustomerProjectionImpl projection = new ReportOrdersByCustomerProjectionImpl();
    		projection.setOrdenId(((Integer) row[0]).intValue());
    		projection.setProducto((String) row[1]);
    		projection.setCantidad(((Integer) row[2]).intValue());
    		projection.setCustomer((String) row[3]);
    		projection.setFechaOrden(((Timestamp) row[4]).toLocalDateTime().toLocalDate());
        	projection.setFechaAttended(((Timestamp) row[5]) == null ? null: ((Timestamp) row[5]).toLocalDateTime().toLocalDate());
    		return projection;
    	}).collect(Collectors.toList());
		
		int totalRows = resultProjection.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalRows);

        return new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	}

}
