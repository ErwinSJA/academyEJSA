package com.ejsa.academy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ejsa.academy.presentation.dto.RegisterCustomerDto;
import com.ejsa.academy.presentation.dto.UpdateCustomerDto;
import com.ejsa.academy.service.interf.CustomerService;
import com.ejsa.academy.utils.JsonLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CustomerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
		
	@Autowired
	private CustomerService customerService;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@BeforeAll
	private void LlenarDatos()
	{
		List<RegisterCustomerDto> customers = null;
		try {
			customers = JsonLoader.loadObjects("src/test/resources/customers.json", RegisterCustomerDto.class);
			for (RegisterCustomerDto customerDto : customers) {
	            customerService.registerCustomer(customerDto);
	        }
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	@Test
	void registerCustomerTestOK() throws Exception {
		RegisterCustomerDto customerDto = new RegisterCustomerDto();
		customerDto.setCustomerName("Nombre Test");
		customerDto.setCustomerAddress("Direccion Test");
		customerDto.setCustomerEmail("test01@gmail.com");
		customerDto.setDocumentType(1);
		customerDto.setDocumentNumber("11111111");
		String customerJson = objectMapper.writeValueAsString(customerDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customerJson))
		.andExpect(status().isCreated())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertFalse(jsonResponse.isEmpty());
		assertEquals("Nombre Test", JsonPath.parse(jsonResponse).read("$.customerName"));
	    assertEquals("11111111", JsonPath.parse(jsonResponse).read("$.documentNumber"));
	}
	
	@Test
	void registerCustomerTestErrorDto() throws Exception {
		RegisterCustomerDto customerDto = new RegisterCustomerDto();
		customerDto.setCustomerName("");
		customerDto.setCustomerAddress("");
		customerDto.setCustomerEmail("test01gmail.com");
		customerDto.setDocumentType(1);
		customerDto.setDocumentNumber("");
		String customerJson = objectMapper.writeValueAsString(customerDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customerJson))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Error registering the customer", messageException);
	}
	
	@Test
	void getCustomerByIdTestOK() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/1")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn();
		
		String jsonCustomer = mvcResult.getResponse().getContentAsString();
		
		assertFalse(jsonCustomer.isEmpty());
		assertEquals("Ricardo Martinez", JsonPath.parse(jsonCustomer).read("$.customerName"));
	    assertEquals("18456325", JsonPath.parse(jsonCustomer).read("$.documentNumber"));
	}
	
	@Test
	void getCustomerByIdTestErrorNotFound() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/989895")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("No customer found", messageException);
	}
	
	@Test
	void updateCustomerTestOK() throws Exception {
		UpdateCustomerDto customerDto = new UpdateCustomerDto();
		customerDto.setCustomerId(1);
		customerDto.setCustomerAddress("Editada direccion Test");
		customerDto.setCustomerEmail("mailedittest01@gmail.com");
		String customerJson = objectMapper.writeValueAsString(customerDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/customers/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customerJson))
		.andExpect(status().isOk())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertFalse(jsonResponse.isEmpty());
		assertEquals(1, (int) JsonPath.parse(jsonResponse).read("$.customerId"));
	    assertEquals("mailedittest01@gmail.com", JsonPath.parse(jsonResponse).read("$.customerEmail"));
	}
	
	@Test
	void updateCustomerTestErrorCustomerNotFound() throws Exception {
		UpdateCustomerDto customerDto = new UpdateCustomerDto();
		customerDto.setCustomerId(989898989);
		customerDto.setCustomerAddress("Editada direccion Test");
		customerDto.setCustomerEmail("mailedittest01@gmail.com");
		String customerJson = objectMapper.writeValueAsString(customerDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/customers/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customerJson))
		.andExpect(status().isNotFound())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Customer not found", messageException);
	}
	
	@Test
	void updateCustomerTestErrorUpdating() throws Exception {
		UpdateCustomerDto customerDto = new UpdateCustomerDto();
		customerDto.setCustomerId(1);
		customerDto.setCustomerAddress("Editada direccion Test");
		customerDto.setCustomerEmail("mailedittest01gmail.com");
		String customerJson = objectMapper.writeValueAsString(customerDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/customers/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customerJson))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Error updating the customer", messageException);
	}
	
	@Test
	void deleteCustomerTestOK() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/delete/2"))
		.andExpect(status().isNoContent())
		.andReturn();
	}
	
	@Test
	void deleteCustomerTestErrorCustomerNotFound() throws Exception {
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/delete/156445"))
		.andExpect(status().isNotFound())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Customer not found", messageException);
	}
	
	@Test
    void getCustomersByPageTestOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
