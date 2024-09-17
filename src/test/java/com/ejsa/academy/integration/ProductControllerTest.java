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

import com.ejsa.academy.presentation.dto.RegisterProductDto;
import com.ejsa.academy.presentation.dto.UpdateProductDto;
import com.ejsa.academy.service.interf.ProductService;
import com.ejsa.academy.utils.JsonLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
		
	@Autowired
	private ProductService productService;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@BeforeAll
	private void LlenarDatos()
	{
		List<RegisterProductDto> products = null;
		try {
			products = JsonLoader.loadObjects("src/test/resources/products.json", RegisterProductDto.class);
			for (RegisterProductDto productDto : products) {
	            productService.registerProduct(productDto);
	        }
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	@Test
	void registerProductTestOK() throws Exception {
		RegisterProductDto productDto = new RegisterProductDto();
		productDto.setCategoryId(2);
		productDto.setProductName("Producto de Test");
		productDto.setProductPrice(257.25);
		productDto.setProductStatus(1);
		String productJson = objectMapper.writeValueAsString(productDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
		.andExpect(status().isCreated())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertFalse(jsonResponse.isEmpty());
		assertEquals("Producto de Test", JsonPath.parse(jsonResponse).read("$.productName"));
	    assertEquals(257.25, (Double) JsonPath.parse(jsonResponse).read("$.productPrice"));
	}
	
	@Test
	void registerProductTestErrorDto() throws Exception {
		RegisterProductDto productDto = new RegisterProductDto();
		productDto.setCategoryId(2);
		productDto.setProductName("");
		productDto.setProductPrice(257.25);
		productDto.setProductStatus(1);
		String productJson = objectMapper.writeValueAsString(productDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Error registering the product", messageException);
	}
	
	@Test
	void getProductByIdTestOK() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn();
		
		String jsonProduct = mvcResult.getResponse().getContentAsString();
		
		assertFalse(jsonProduct.isEmpty());
		assertEquals("PC Asus i7, 32GB, 1TB, NVIDIA 4060 12GB", JsonPath.parse(jsonProduct).read("$.productName"));
	    assertEquals(6500, (Double) JsonPath.parse(jsonProduct).read("$.productPrice"));
	}
	
	@Test
	void getProductByIdTestErrorNotFound() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/989895")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Product not found", messageException);
	}
	
	@Test
	void updateProductTestOK() throws Exception {
		UpdateProductDto productDto = new UpdateProductDto();
		productDto.setProductId(8);
		productDto.setCategoryId(4);
		productDto.setProductName("Nuevo edit Name");
		productDto.setProductPrice(350.00);
		productDto.setProductStatus(1);
		String productJson = objectMapper.writeValueAsString(productDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/products/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
		.andExpect(status().isOk())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertFalse(jsonResponse.isEmpty());
		assertEquals(8, (int) JsonPath.parse(jsonResponse).read("$.productId"));
	    assertEquals("Nuevo edit Name", JsonPath.parse(jsonResponse).read("$.productName"));
	}
	
	@Test
	void updateProductTestErrorProductNotFound() throws Exception {
		UpdateProductDto productDto = new UpdateProductDto();
		productDto.setProductId(78454);
		productDto.setCategoryId(4);
		productDto.setProductName("Nuevo edit Name");
		productDto.setProductPrice(350.00);
		productDto.setProductStatus(1);
		String productJson = objectMapper.writeValueAsString(productDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/products/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
		.andExpect(status().isNotFound())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Product not found", messageException);
	}
	
	@Test
	void updateProductTestErrorUpdating() throws Exception {
		UpdateProductDto productDto = new UpdateProductDto();
		productDto.setProductId(8);
		productDto.setCategoryId(4);
		productDto.setProductName("");
		productDto.setProductPrice(350.00);
		productDto.setProductStatus(1);
		String productJson = objectMapper.writeValueAsString(productDto);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/products/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Error updating the product", messageException);
	}
	
	@Test
	void deleteProductTestOK() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/delete/2"))
		.andExpect(status().isNoContent())
		.andReturn();
	}
	
	@Test
	void deleteProductTestErrorProductNotFound() throws Exception {
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/delete/156445"))
		.andExpect(status().isNotFound())
		.andReturn();
		
		String messageException = mvcResult.getResolvedException().getMessage();
		
		assertEquals("Product not found", messageException);
	}
	
	@Test
    void getProductsByPageTestByProductStatusOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
        		.param("productName", "Monitor")
        		.param("status", "1")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
	
	@Test
    void getProductsByPageTestAllOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
