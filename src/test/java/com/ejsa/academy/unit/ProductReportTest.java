package com.ejsa.academy.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.domain.Pageable;

import com.ejsa.academy.domain.dao.impl.ProductDaoImpl;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.impl.product.ReportProductsProjectionImpl;
import com.ejsa.academy.model.projection.interf.product.ReportProductsProjection;
import com.ejsa.academy.presentation.controller.ProductController;
import com.ejsa.academy.service.impl.ProductServiceImpl;
import com.ejsa.academy.utils.SimplePageResponse;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProductReportTest {
	
	@Nested
	class testController {
		
		@Mock
	    private ProductServiceImpl productService;
		
		@InjectMocks
		private ProductController productController;
		
		private MockMvc mockMvc;
		
		@BeforeEach
	    public void setup() {
			MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();        
	    }
		
		@Test
		void getReportProductsTestOK() throws Exception {
			
			Pageable pageable = PageRequest.of(0, 5);
			
			List<ReportProductsProjection> resultProjection = new ArrayList<>();
			
			resultProjection.add(new ReportProductsProjectionImpl() {
				{ 
					setProductId(1);
					setCategoria("Monitores");
					setProducto("Teros 450GM");
					setPrecioUnitario(850.00);
					setEstado("Activo");
				}			
			});
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportProductsProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(productService.findReportProducts(pageable)).thenReturn(pageData);
					
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/reportProducts")
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
		    ObjectMapper objectMapper = new ObjectMapper();

		    // Deserializar la respuesta JSON en SimplePageResponse
		    String jsonResponse = mvcResult.getResponse().getContentAsString();
		    JavaType type = objectMapper.getTypeFactory().constructParametricType(SimplePageResponse.class, ReportProductsProjectionImpl.class);
		    SimplePageResponse<ReportProductsProjectionImpl> response = objectMapper.readValue(jsonResponse, type);
		    List<ReportProductsProjectionImpl> productList = response.getContent();
		    
		    assertNotNull(productList);
		    assertEquals(1, productList.size());
		    
		    ReportProductsProjection product = productList.get(0);
		    assertEquals(1, product.getProductId());
		    assertEquals("Teros 450GM", product.getProducto());
		}
	}
	
	@Nested
	class testService {
		
		@Mock
		private ProductDaoImpl productDao;
		
		@InjectMocks
	    private ProductServiceImpl productService;
		
		@BeforeEach
		void setup() {
          MockitoAnnotations.openMocks(this);
		}
		
		@Test
	    void findReportProductsTestOK() {
			Pageable pageable = PageRequest.of(0, 5);
			
			List<ReportProductsProjection> resultProjection = new ArrayList<>();
			
			resultProjection.add(new ReportProductsProjectionImpl() {
				{ 
					setProductId(1);
					setCategoria("Monitores");
					setProducto("Teros 450GM");
					setPrecioUnitario(850.00);
					setEstado("Activo");
				}			
			});
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportProductsProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(productDao.findReportProducts(pageable)).thenReturn(pageData);

	        Page<ReportProductsProjection> result = productService.findReportProducts(pageable);

	        assertNotNull(result);
	        assertEquals(1, result.getTotalElements());
	        assertEquals(1, result.getContent().size());

	        ReportProductsProjection projection = result.getContent().get(0);
	        assertEquals(1, projection.getProductId());
	        assertEquals("Teros 450GM", projection.getProducto());
	    }
		
		@Test
	    void findReportProductsTestErrorNotFound() {
			Pageable pageable = PageRequest.of(0, 5);
			
			List<ReportProductsProjection> resultProjection = new ArrayList<>();
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportProductsProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(productDao.findReportProducts(pageable)).thenReturn(pageData);

			APIException thrownException = assertThrows(APIException.class, () -> {
				productService.findReportProducts(pageable);
		    });

		    assertEquals("No data", thrownException.getMessage());
		    assertEquals(HttpStatus.NOT_FOUND, thrownException.getHttpStatus());
	    }
		
		@Test
	    void findReportProductsTestErrorInternalServer() {
			Pageable pageable = PageRequest.of(0, 5);

			when(productDao.findReportProducts(pageable)).thenReturn(null);

			APIException thrownException = assertThrows(APIException.class, () -> {
				productService.findReportProducts(pageable);
		    });

		    assertEquals("Error executing the product report", thrownException.getMessage());
		    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getHttpStatus());
	    }
	}
	
	@Nested
	class testDAO {
		
		@Mock
	    private EntityManager entityManager;
		
		@InjectMocks
		private ProductDaoImpl productDao;
		
		@BeforeEach
		void setup() {
          MockitoAnnotations.openMocks(this);
		}
		
		@Test
	    void findReportProductsDAOTestOK() {
	        
			Pageable pageable = PageRequest.of(0, 5);
			
			Object[] fila = new Object[]{1, "Monitores", "Teros 450GM", BigDecimal.valueOf(850.00), "Activo"};
	        List<Object[]> simulatedQueryResult = Collections.singletonList(fila);

	        // Mockeamos el StoredProcedureQuery
	        StoredProcedureQuery storedProcedureQuery = mock(StoredProcedureQuery.class);
	        when(storedProcedureQuery.getResultList()).thenReturn(simulatedQueryResult);

	        // Mockeamos el EntityManager
	        when(entityManager.createStoredProcedureQuery("uspRptProductos")).thenReturn(storedProcedureQuery);

	        Page<ReportProductsProjection> pageProducts = productDao.findReportProducts(pageable);

	        assertNotNull(pageProducts);
	        assertEquals(1, pageProducts.getTotalElements());
	        assertEquals(1, pageProducts.getContent().size());

	        ReportProductsProjection projection = pageProducts.getContent().get(0);
	        assertEquals(1, projection.getProductId());
	        assertEquals("Teros 450GM", projection.getProducto());
	    }	
		
	}
}
