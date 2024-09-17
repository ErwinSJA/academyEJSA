package com.ejsa.academy.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.ejsa.academy.domain.dao.impl.OrderDaoImpl;
import com.ejsa.academy.domain.repository.OrderPendingBimonthlyViewRepository;
import com.ejsa.academy.domain.repository.OrderRejectedMonthlyViewRepository;
import com.ejsa.academy.domain.repository.OrderRepository;
import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersProjection;
import com.ejsa.academy.presentation.controller.OrderController;
import com.ejsa.academy.model.projection.impl.order.ReportOrdersByCustomerProjectionImpl;
import com.ejsa.academy.model.projection.impl.order.ReportOrdersProjectionImpl;
import com.ejsa.academy.model.projection.interf.customer.CustomerProjection;
import com.ejsa.academy.model.projection.interf.order.OrderDetailsProjection;
import com.ejsa.academy.model.projection.interf.order.OrderPendingBimonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.OrderProjection;
import com.ejsa.academy.model.projection.interf.order.OrderRejectedMonthlyViewProjection;
import com.ejsa.academy.model.projection.interf.order.ReportOrdersByCustomerProjection;
import com.ejsa.academy.service.impl.OrderServiceImpl;
import com.jayway.jsonpath.JsonPath;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class OrderReportTest {
	
	@Nested
	class testController{
		@Mock
		private OrderServiceImpl orderService;
		
		@InjectMocks
		private OrderController orderController;
		
		private MockMvc mockMvc;
	
		@BeforeEach
	    public void setup() {
			MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();        
	    }
		
		@Test
	    void getOrdersTestOK() throws Exception {
			Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.ASC,"orderId"));

			OrderProjection orderProjection = new OrderProjection() {
	            @Override
	            public Integer getOrderId() { return 1; }
	            @Override
	            public LocalDate getOrderDate() { return LocalDate.now(); }
	            @Override
	            public Integer getTotalOrderAmount() { return 10; }
	            @Override
	            public Double getTotalOrderPrice() { return 2500.00; }
	            @Override
	            public LocalDate getServiceOrderDate() { return LocalDate.now(); }
	            @Override
	            public Integer getOrderStatus() { return 1; }
	            @Override
	            public List<OrderDetailsProjection> getOrderDetails() { return Collections.emptyList(); }
				@Override
				public CustomerProjection getCustomer() { return null;}
			};
			
			List<OrderProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderService.getOrders(pageable)).thenReturn(pageData);
	
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders")
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
			String jsonResponse = mvcResult.getResponse().getContentAsString();
			
			assertEquals(1, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].orderId"));
			assertEquals(10, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].totalOrderAmount"));
			assertEquals(2500.00, (Double) JsonPath.parse(jsonResponse).read("$.content[0].totalOrderPrice"));
	    }
		
		@Test
	    void getMonthlyViewOrdersRejectedTestOK() throws Exception {
			Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.DESC,"ordenId"));

			OrderRejectedMonthlyViewProjection orderProjection = new OrderRejectedMonthlyViewProjection() {
				@Override
				public Integer getOrdenId() {
					return 7;
				}
				@Override
				public Integer getCantidad() {
					return 45;
				}
				@Override
				public Double getPrecioTotal() {
					return 3800.25;
				}
				@Override
				public LocalDate getFechaOrden() {
					return null;
				}
				@Override
				public LocalDate getFechaAttended() {
					return null;
				}
				@Override
				public String getCustomer() {
					return null;
				}
	        };
			
			List<OrderRejectedMonthlyViewProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderRejectedMonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderService.getMonthlyViewOrdersRejected(pageable)).thenReturn(pageData);
	
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/view/rejected/monthly")
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
			String jsonResponse = mvcResult.getResponse().getContentAsString();
			
			assertEquals(7, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].ordenId"));
			assertEquals(45, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].cantidad"));
			assertEquals(3800.25, (Double) JsonPath.parse(jsonResponse).read("$.content[0].precioTotal"));
	    }
		
		@Test
	    void getBimonthlyViewOrdersPendingTestOK() throws Exception {
			Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.DESC,"ordenId"));

			OrderPendingBimonthlyViewProjection orderProjection = new OrderPendingBimonthlyViewProjection() {
				@Override
				public Integer getOrdenId() {
					return 7;
				}
				@Override
				public Integer getCantidad() {
					return 45;
				}
				@Override
				public Double getPrecioTotal() {
					return 3800.25;
				}
				@Override
				public LocalDate getFechaOrden() {
					return null;
				}
				@Override
				public LocalDate getFechaAttended() {
					return null;
				}
				@Override
				public String getCustomer() {
					return null;
				}
	        };
			
			List<OrderPendingBimonthlyViewProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderPendingBimonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderService.getBimonthlyViewOrdersPending(pageable)).thenReturn(pageData);
	
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/view/pending/bimonthly")
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
			String jsonResponse = mvcResult.getResponse().getContentAsString();
			
			assertEquals(7, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].ordenId"));
			assertEquals(45, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].cantidad"));
			assertEquals(3800.25, (Double) JsonPath.parse(jsonResponse).read("$.content[0].precioTotal"));
	    }
		
		@Test
	    void getMonthlyReportOrdersReceivedTestOK() throws Exception {
			Pageable pageable = PageRequest.of(0, 5);
			
			List<ReportOrdersProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(25);
				setCustomer("Angel Carranza");
				setCantidad(20);
				setPrecioTotal(1450.00);
				setEstado("Recibido");
			}
	        });
			
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(32);
				setCustomer("Romina Torres");
				setCantidad(10);
				setPrecioTotal(820.00);
				setEstado("Recibido");
			}
	        });
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderService.getMonthlyReportOrdersReceived(pageable)).thenReturn(pageData);
	
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/report/received/monthly")
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
			String jsonResponse = mvcResult.getResponse().getContentAsString();
			
			assertEquals(25, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].ordenId"));
			assertEquals("Angel Carranza", JsonPath.parse(jsonResponse).read("$.content[0].customer"));
			assertEquals(32, (Integer) JsonPath.parse(jsonResponse).read("$.content[1].ordenId"));
			assertEquals("Romina Torres", JsonPath.parse(jsonResponse).read("$.content[1].customer"));		
	    }
		
		@Test
	    void getQuaterlyReportOrdersAttendedTestOK() throws Exception {
			Pageable pageable = PageRequest.of(0, 5);
			
			List<ReportOrdersProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(25);
				setCustomer("Angel Carranza");
				setCantidad(20);
				setPrecioTotal(1450.00);
				setEstado("Recibido");
			}
	        });
			
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(32);
				setCustomer("Romina Torres");
				setCantidad(10);
				setPrecioTotal(820.00);
				setEstado("Recibido");
			}
	        });
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderService.getQuaterlyReportOrdersAttended(pageable)).thenReturn(pageData);
	
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/report/attended/quaterly")
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
			String jsonResponse = mvcResult.getResponse().getContentAsString();
			
			assertEquals(25, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].ordenId"));
			assertEquals("Angel Carranza", JsonPath.parse(jsonResponse).read("$.content[0].customer"));
			assertEquals(32, (Integer) JsonPath.parse(jsonResponse).read("$.content[1].ordenId"));
			assertEquals("Romina Torres", JsonPath.parse(jsonResponse).read("$.content[1].customer"));		
	    }
		
		@Test
	    void getReportOrdersByCustomerTestOK() throws Exception {
			Pageable pageable = PageRequest.of(0, 5);
			String customerName = "Belen";
			
			List<ReportOrdersByCustomerProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(new ReportOrdersByCustomerProjectionImpl() {{
				setOrdenId(5);
				setCustomer("Belen Aguirre");
				setCantidad(26);
				setProducto("Monitor Teros 27 pulgadas");
			}
	        });
			
			resultProjection.add(new ReportOrdersByCustomerProjectionImpl() {{
				setOrdenId(43);
				setCustomer("Belen Torres");
				setCantidad(12);
				setProducto("Teclado Ryzen 7000");
			}
	        });
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersByCustomerProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderService.getReportOrdersByCustomer(customerName, pageable)).thenReturn(pageData);
	
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/report/all/monthly/byCustomer")
					.param("customerName", customerName)
					.param("page", "0")
					.param("size", "5")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
			
			String jsonResponse = mvcResult.getResponse().getContentAsString();
			
			assertEquals(5, (Integer) JsonPath.parse(jsonResponse).read("$.content[0].ordenId"));
			assertEquals("Belen Aguirre", JsonPath.parse(jsonResponse).read("$.content[0].customer"));
			assertEquals(43, (Integer) JsonPath.parse(jsonResponse).read("$.content[1].ordenId"));
			assertEquals("Belen Torres", JsonPath.parse(jsonResponse).read("$.content[1].customer"));
	    }
	}

	@Nested
	class testService{

		@Mock
		private OrderDaoImpl orderDAO;
		
		@Mock
		private OrderRepository orderRepository;
		
		@Mock
		private OrderRejectedMonthlyViewRepository orderRejectedMonthlyViewRepository;
		
		@Mock
		private OrderPendingBimonthlyViewRepository orderPendingBimonthlyViewRepository;
		
		@InjectMocks
		private OrderServiceImpl orderService;
		
		@BeforeEach
		void setup() {
          MockitoAnnotations.openMocks(this);
		}
		
		@Test
	    void getOrdersTestOK() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.ASC, "orderId");
			
			OrderProjection orderProjection = new OrderProjection() {
	            @Override
	            public Integer getOrderId() { return 1; }
	            @Override
	            public LocalDate getOrderDate() { return LocalDate.now(); }
	            @Override
	            public Integer getTotalOrderAmount() { return 10; }
	            @Override
	            public Double getTotalOrderPrice() { return 2500.00; }
	            @Override
	            public LocalDate getServiceOrderDate() { return LocalDate.now(); }
	            @Override
	            public Integer getOrderStatus() { return 1; }
	            @Override
	            public List<OrderDetailsProjection> getOrderDetails() { return Collections.emptyList(); }
				@Override
				public CustomerProjection getCustomer() { return null;
				}
	        };
			
			List<OrderProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderRepository.findAllBy(pageable)).thenReturn(pageData);
	
	        Page<OrderProjection> result = orderService.getOrders(pageable);
	
	        assertNotNull(result);
	        assertEquals(1, result.getTotalElements());
	        assertEquals(1, result.getContent().get(0).getOrderId());
	        assertEquals(2500.00, result.getContent().get(0).getTotalOrderPrice());
	        assertEquals(10, result.getContent().get(0).getTotalOrderAmount());
	    }
		
		@Test
	    void getMonthlyViewOrdersRejectedTestOK() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "ordenId");
			
			OrderRejectedMonthlyViewProjection orderProjection = new OrderRejectedMonthlyViewProjection() {
				@Override
				public Integer getOrdenId() {
					return 7;
				}
				@Override
				public Integer getCantidad() {
					return 45;
				}
				@Override
				public Double getPrecioTotal() {
					return 3800.25;
				}
				@Override
				public LocalDate getFechaOrden() {
					return null;
				}
				@Override
				public LocalDate getFechaAttended() {
					return null;
				}
				@Override
				public String getCustomer() {
					return null;
				}
	        };
			
			List<OrderRejectedMonthlyViewProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderRejectedMonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderRejectedMonthlyViewRepository.findAllOrderRejectedMonthlyViewProjectionBy(pageable)).thenReturn(pageData);
	
	        Page<OrderRejectedMonthlyViewProjection> result = orderService.getMonthlyViewOrdersRejected(pageable);
	
	        assertNotNull(result);
	        assertEquals(1, result.getTotalElements());
	        assertEquals(7, result.getContent().get(0).getOrdenId());
	        assertEquals(3800.25, result.getContent().get(0).getPrecioTotal());
	        assertEquals(45, result.getContent().get(0).getCantidad());
	    }
		
		@Test
	    void getMonthlyViewOrdersRejectedTestErrorNotFound() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "ordenId");
			
			List<OrderRejectedMonthlyViewProjection> resultProjection = new ArrayList<>();
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderRejectedMonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderRejectedMonthlyViewRepository.findAllOrderRejectedMonthlyViewProjectionBy(pageable)).thenReturn(pageData);
	
	        APIException thrownException = assertThrows(APIException.class, () -> {
	        	orderService.getMonthlyViewOrdersRejected(pageable);
		    });
	
		    assertEquals("No data in view", thrownException.getMessage());
		    assertEquals(HttpStatus.NOT_FOUND, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getMonthlyViewOrdersRejectedTestErrorServer() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "ordenId");
			
			when(orderRejectedMonthlyViewRepository.findAllOrderRejectedMonthlyViewProjectionBy(pageable)).thenReturn(null);
	
	        APIException thrownException = assertThrows(APIException.class, () -> {
	        	orderService.getMonthlyViewOrdersRejected(pageable);
		    });
	
		    assertEquals("Error executing the Monthly Rejected View", thrownException.getMessage());
		    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getBimonthlyViewOrdersPendingTestOK() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "ordenId");
			
			OrderPendingBimonthlyViewProjection orderProjection = new OrderPendingBimonthlyViewProjection() {
				@Override
				public Integer getOrdenId() {
					return 7;
				}
				@Override
				public Integer getCantidad() {
					return 45;
				}
				@Override
				public Double getPrecioTotal() {
					return 3800.25;
				}
				@Override
				public LocalDate getFechaOrden() {
					return null;
				}
				@Override
				public LocalDate getFechaAttended() {
					return null;
				}
				@Override
				public String getCustomer() {
					return null;
				}
	        };
			
			List<OrderPendingBimonthlyViewProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(orderProjection);
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderPendingBimonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderPendingBimonthlyViewRepository.findAllOrderPendingBimonthlyViewProjectionBy(pageable)).thenReturn(pageData);
	
	        Page<OrderPendingBimonthlyViewProjection> result = orderService.getBimonthlyViewOrdersPending(pageable);
	
	        assertNotNull(result);
	        assertEquals(1, result.getTotalElements());
	        assertEquals(7, result.getContent().get(0).getOrdenId());
	        assertEquals(3800.25, result.getContent().get(0).getPrecioTotal());
	        assertEquals(45, result.getContent().get(0).getCantidad());
	    }
		
		@Test
	    void getBimonthlyViewOrdersPendingTestErrorNotFound() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "ordenId");
			
			List<OrderPendingBimonthlyViewProjection> resultProjection = new ArrayList<>();
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<OrderPendingBimonthlyViewProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderPendingBimonthlyViewRepository.findAllOrderPendingBimonthlyViewProjectionBy(pageable)).thenReturn(pageData);
	
	        APIException thrownException = assertThrows(APIException.class, () -> {
	        	orderService.getBimonthlyViewOrdersPending(pageable);
		    });
	
		    assertEquals("No data in view", thrownException.getMessage());
		    assertEquals(HttpStatus.NOT_FOUND, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getBimonthlyViewOrdersPendingTestErrorServer() {
			Pageable pageable = PageRequest.of(0, 5).withSort(Direction.DESC, "ordenId");
			
			when(orderPendingBimonthlyViewRepository.findAllOrderPendingBimonthlyViewProjectionBy(pageable)).thenReturn(null);
	
	        APIException thrownException = assertThrows(APIException.class, () -> {
	        	orderService.getBimonthlyViewOrdersPending(pageable);
		    });
	
		    assertEquals("Error executing the Bimonthly Pending View", thrownException.getMessage());
		    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getMonthlyReportOrdersReceivedTestOK() {
			Pageable pageable = PageRequest.of(0, 5);
	
			List<ReportOrdersProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(25);
				setCustomer("Angel Carranza");
				setCantidad(20);
				setPrecioTotal(1450.00);
				setEstado("Recibido");
			}
	        });
			
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(32);
				setCustomer("Romina Torres");
				setCantidad(10);
				setPrecioTotal(820.00);
				setEstado("Recibido");
			}
	        });
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderDAO.findReportOrders("uspRptOrdenesRecibidasMensual", pageable)).thenReturn(pageData);
	
	        Page<ReportOrdersProjection> result = orderService.getMonthlyReportOrdersReceived(pageable);
	
	        assertNotNull(result);
	        assertEquals(2, result.getTotalElements());
	        assertEquals(25, result.getContent().get(0).getOrdenId());
	        assertEquals("Angel Carranza", result.getContent().get(0).getCustomer());
	        assertEquals(32, result.getContent().get(1).getOrdenId());
	        assertEquals("Romina Torres", result.getContent().get(1).getCustomer());
	    }
		
		@Test
	    void getMonthlyReportOrdersReceivedTestErrorNotFound() {
			Pageable pageable = PageRequest.of(0, 5);
	
			List<ReportOrdersProjection> resultProjection = new ArrayList<>();		
	
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderDAO.findReportOrders("uspRptOrdenesRecibidasMensual", pageable)).thenReturn(pageData);
			
			APIException thrownException = assertThrows(APIException.class, () -> {
				orderService.getMonthlyReportOrdersReceived(pageable);
		    });
	
		    assertEquals("No data", thrownException.getMessage());
		    assertEquals(HttpStatus.NOT_FOUND, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getMonthlyReportOrdersReceivedTestErrorInternalServer() {
			Pageable pageable = PageRequest.of(0, 5);
	
			when(orderDAO.findReportOrders("uspRptOrdenesRecibidasMensual", pageable)).thenReturn(null);
			
			APIException thrownException = assertThrows(APIException.class, () -> {
				orderService.getMonthlyReportOrdersReceived(pageable);
		    });
	
		    assertEquals("Error executing the monthly report", thrownException.getMessage());
		    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getQuaterlyReportOrdersAttendedTestOK() {
			Pageable pageable = PageRequest.of(0, 5);
			
			List<ReportOrdersProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(14);
				setCustomer("Belen Aguirre");
				setCantidad(65);
				setPrecioTotal(12150.00);
				setEstado("Atendido");
			}
	        });
			
			resultProjection.add(new ReportOrdersProjectionImpl() {{
				setOrdenId(16);
				setCustomer("Manuel Vera");
				setCantidad(4);
				setPrecioTotal(320.00);
				setEstado("Atendido");
			}
	        });
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderDAO.findReportOrders("uspRptOrdenesAtendidas3Meses", pageable)).thenReturn(pageData);
	
	        Page<ReportOrdersProjection> result = orderService.getQuaterlyReportOrdersAttended(pageable);
	
	        assertNotNull(result);
	        assertEquals(2, result.getTotalElements());
	        assertEquals(14, result.getContent().get(0).getOrdenId());
	        assertEquals("Belen Aguirre", result.getContent().get(0).getCustomer());
	        assertEquals(16, result.getContent().get(1).getOrdenId());
	        assertEquals("Manuel Vera", result.getContent().get(1).getCustomer());
	    }
		
		@Test
	    void getQuaterlyReportOrdersAttendedTestErrorNotFound() {
			Pageable pageable = PageRequest.of(0, 5);
	
			List<ReportOrdersProjection> resultProjection = new ArrayList<>();		
	
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderDAO.findReportOrders("uspRptOrdenesAtendidas3Meses", pageable)).thenReturn(pageData);
			
			APIException thrownException = assertThrows(APIException.class, () -> {
				orderService.getQuaterlyReportOrdersAttended(pageable);
		    });
	
		    assertEquals("No data", thrownException.getMessage());
		    assertEquals(HttpStatus.NOT_FOUND, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getQuaterlyReportOrdersAttendedTestErrorInternalServer() {
			Pageable pageable = PageRequest.of(0, 5);
	
			when(orderDAO.findReportOrders("uspRptOrdenesAtendidas3Meses", pageable)).thenReturn(null);
			
			APIException thrownException = assertThrows(APIException.class, () -> {
				orderService.getQuaterlyReportOrdersAttended(pageable);
		    });
	
		    assertEquals("Error executing the quaterly report", thrownException.getMessage());
		    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getHttpStatus());
	    }
	
		@Test
	    void getReportOrdersByCustomerTestOK() {
			Pageable pageable = PageRequest.of(0, 5);
			String customerName = "Belen";
			
			List<ReportOrdersByCustomerProjection> resultProjection = new ArrayList<>();		
			resultProjection.add(new ReportOrdersByCustomerProjectionImpl() {{
				setOrdenId(5);
				setCustomer("Belen Aguirre");
				setCantidad(26);
				setProducto("Monitor Teros 27 pulgadas");
			}
	        });
			
			resultProjection.add(new ReportOrdersByCustomerProjectionImpl() {{
				setOrdenId(43);
				setCustomer("Belen Torres");
				setCantidad(12);
				setProducto("Teclado Ryzen 7000");
			}
	        });
			
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersByCustomerProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderDAO.findReportOrdersByCustomer(customerName, pageable)).thenReturn(pageData);
	
	        Page<ReportOrdersByCustomerProjection> result = orderService.getReportOrdersByCustomer(customerName, pageable);
	
	        assertNotNull(result);
	        assertEquals(2, result.getTotalElements());
	        assertEquals(5, result.getContent().get(0).getOrdenId());
	        assertEquals("Belen Aguirre", result.getContent().get(0).getCustomer());
	        assertEquals(43, result.getContent().get(1).getOrdenId());
	        assertEquals("Belen Torres", result.getContent().get(1).getCustomer());
	    }
		
		@Test
	    void getReportOrdersByCustomerTestErrorNotFound() {
			Pageable pageable = PageRequest.of(0, 5);
	
			List<ReportOrdersByCustomerProjection> resultProjection = new ArrayList<>();		
	
	    	int totalRows = resultProjection.size();
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), totalRows);
			
	        Page<ReportOrdersByCustomerProjection> pageData = new PageImpl<>(resultProjection.subList(start, end), pageable, totalRows);
	        
			when(orderDAO.findReportOrdersByCustomer("Belen", pageable)).thenReturn(pageData);
			
			APIException thrownException = assertThrows(APIException.class, () -> {
				orderService.getReportOrdersByCustomer("Belen", pageable);
		    });
	
		    assertEquals("No data", thrownException.getMessage());
		    assertEquals(HttpStatus.NOT_FOUND, thrownException.getHttpStatus());
	    }
		
		@Test
	    void getReportOrdersByCustomerTestErrorInternalServer() {
			Pageable pageable = PageRequest.of(0, 5);
	
			when(orderDAO.findReportOrdersByCustomer("Belen", pageable)).thenReturn(null);
			
			APIException thrownException = assertThrows(APIException.class, () -> {
				orderService.getReportOrdersByCustomer("Belen", pageable);
		    });
	
		    assertEquals("Error executing the report orders by customer", thrownException.getMessage());
		    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getHttpStatus());
	    }
		
	}
	
	@Nested
	class testDAO{
		
		@Mock
	    private EntityManager entityManager;
		
		@InjectMocks
		private OrderDaoImpl orderDAOUnit;
		
		@BeforeEach
		void setup() {
          MockitoAnnotations.openMocks(this);
		}
		
		@Test
		void findReportOrdersTestOK() {
			Pageable pageable = PageRequest.of(0, 5);
			
			Object[] fila = new Object[]{25, "Angel Carranza", 20, BigDecimal.valueOf(1450.00), Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), "Recibido"};
			List<Object[]> simulatedQueryResult = Collections.singletonList(fila);
	
			// Mockeamos el StoredProcedureQuery
			StoredProcedureQuery storedProcedureQuery = mock(StoredProcedureQuery.class);
			when(storedProcedureQuery.getResultList()).thenReturn(simulatedQueryResult);
	
			// Mockeamos el EntityManager
			when(entityManager.createStoredProcedureQuery("uspRptOrdenesRecibidasMensual")).thenReturn(storedProcedureQuery);
			
			Page<ReportOrdersProjection> resultProjection =  orderDAOUnit.findReportOrders("uspRptOrdenesRecibidasMensual", pageable);
			ReportOrdersProjection reportOrder = resultProjection.getContent().get(0);
			
	        assertNotNull(resultProjection);
	        assertEquals(1, resultProjection.getTotalElements());        
	        assertEquals(25, reportOrder.getOrdenId());
	        assertEquals("Angel Carranza", reportOrder.getCustomer());
	    }
		
		@Test
		void findReportOrdersByCustomerTestOK() {
			Pageable pageable = PageRequest.of(0, 5);
			
			Object[] fila = new Object[]{84, "Monitor Teros 27", 10, "Belen Aguirre", Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now())};
			List<Object[]> simulatedQueryResult = Collections.singletonList(fila);

			// Mockeamos el StoredProcedureQuery
			StoredProcedureQuery storedProcedureQuery = mock(StoredProcedureQuery.class);
			when(storedProcedureQuery.getResultList()).thenReturn(simulatedQueryResult);

			// Mockeamos el EntityManager
			when(entityManager.createStoredProcedureQuery("uspRptOrdenProductoMensualByCliente")).thenReturn(storedProcedureQuery);
			
			Page<ReportOrdersByCustomerProjection> resultProjection =  orderDAOUnit.findReportOrdersByCustomer("Belen", pageable);
			ReportOrdersByCustomerProjection reportOrder = resultProjection.getContent().get(0);
			
	        assertNotNull(resultProjection);
	        assertEquals(1, resultProjection.getTotalElements());        
	        assertEquals(84, reportOrder.getOrdenId());
	        assertEquals("Monitor Teros 27", reportOrder.getProducto());
	        assertEquals("Belen Aguirre", reportOrder.getCustomer());
	    }

	}
}