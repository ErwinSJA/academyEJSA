package com.ejsa.academy.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ejsa.academy.domain.repository.CustomerOrdersMonthlyViewRepository;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.customer.CustomerOrdersMonthlyViewProjection;
import com.ejsa.academy.presentation.controller.CustomerController;
import com.ejsa.academy.service.impl.CustomerServiceImpl;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CustomerOrderReportTest {

	@Nested
	class testController{
		@Mock
		private CustomerServiceImpl customerService;
		
		@InjectMocks
		private CustomerController customerController;
		
		private MockMvc mockMvc;
		
		@BeforeEach
	    public void setup() {
			MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();        
	    }
		
		@Test
		void getMonthlyViewCustomerOrdersTestOK() throws Exception{
			
			Sort sort = Sort.by(
				    Sort.Order.desc("customerName"),
				    Sort.Order.desc("totalOrdenes"));
			
			Pageable pageable = PageRequest.of(0, 5, sort);
			
			CustomerOrdersMonthlyViewProjection orderProjection = new CustomerOrdersMonthlyViewProjection() {

				@Override
				public Integer getCustomerId() {
					return 3;
				}

				@Override
				public String getCustomerName() {
					return "Tadeo Sanchez";
				}

				@Override
				public String getProducto() {
					return "Laptop Lenovo 13";
				}

				@Override
				public Integer getTotalOrdenes() {
					return 8;
				}
				
	        };
	        
	        List<CustomerOrdersMonthlyViewProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<CustomerOrdersMonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(customerService.getMonthlyViewCustomerOrders(pageable)).thenReturn(pageData);
			
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/view/orders")
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
			String jsonResponse = mvcResult.getResponse().getContentAsString();
			
			assertEquals(3, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].customerId"));
			assertEquals("Tadeo Sanchez", JsonPath.parse(jsonResponse).read("$.content[0].customerName"));
			assertEquals("Laptop Lenovo 13", JsonPath.parse(jsonResponse).read("$.content[0].producto"));
			assertEquals(8, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].totalOrdenes"));
		}
		
		
	}
	
	@Nested
	class testService{
		
		@Mock
		private CustomerOrdersMonthlyViewRepository customerOrdersMonthlyViewRepository;
		
		@InjectMocks
		private CustomerServiceImpl customerServiceImpl;
		
		@BeforeEach
		void setup() {
          MockitoAnnotations.openMocks(this);
		}
		
		@Test
	    void getMonthlyViewCustomerOrdersTestOK() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "customerName,totalOrdenes");
			
			CustomerOrdersMonthlyViewProjection orderProjection = new CustomerOrdersMonthlyViewProjection() {

				@Override
				public Integer getCustomerId() {
					return 3;
				}

				@Override
				public String getCustomerName() {
					return "Tadeo Sanchez";
				}

				@Override
				public String getProducto() {
					return "Laptop Lenovo 13";
				}

				@Override
				public Integer getTotalOrdenes() {
					return 8;
				}
				
	        };
			
			List<CustomerOrdersMonthlyViewProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<CustomerOrdersMonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(customerOrdersMonthlyViewRepository.findAllCustomerOrdersMonthlyViewProjectionBy(pageable)).thenReturn(pageData);
	
	        Page<CustomerOrdersMonthlyViewProjection> result = customerServiceImpl.getMonthlyViewCustomerOrders(pageable);
	
	        assertNotNull(result);
	        assertEquals(1, result.getTotalElements());
	        assertEquals(3, result.getContent().get(0).getCustomerId());
	        assertEquals("Tadeo Sanchez", result.getContent().get(0).getCustomerName());
	        assertEquals("Laptop Lenovo 13", result.getContent().get(0).getProducto());
	        assertEquals(8, result.getContent().get(0).getTotalOrdenes());
	    }
		
		@Test
	    void getMonthlyViewCustomerOrdersTestErrorNotFound() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "customerName,totalOrdenes");
			
			List<CustomerOrdersMonthlyViewProjection> resultProjection = new ArrayList<>();	
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<CustomerOrdersMonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(customerOrdersMonthlyViewRepository.findAllCustomerOrdersMonthlyViewProjectionBy(pageable)).thenReturn(pageData);
	
	
	        APIException thrownException = assertThrows(APIException.class, () -> {
	        	customerServiceImpl.getMonthlyViewCustomerOrders(pageable);
		    });
	
		    assertEquals("No data in view", thrownException.getMessage());
		    assertEquals(HttpStatus.NOT_FOUND, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getMonthlyViewCustomerOrdersTestErrorInternalServer() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "customerName,totalOrdenes");


			when(customerOrdersMonthlyViewRepository.findAllCustomerOrdersMonthlyViewProjectionBy(pageable)).thenReturn(null);
	
	
	        APIException thrownException = assertThrows(APIException.class, () -> {
	        	customerServiceImpl.getMonthlyViewCustomerOrders(pageable);
		    });
	
	        assertEquals("Error executing the Monthly Customer Orders View", thrownException.getMessage());
		    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getHttpStatus());
	    }		
	}
}
