package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class) // Carrega somente o contexto da classe principal
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean // carrega os contexto parcialmente, mas substitui por um componente mockado
	private ProductService service;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;

	@BeforeEach
	void setUp() throws Exception {

		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));

		when(service.findAllPaged(any())).thenReturn(page); // any = vem do argumentMatchers - significa qualquer
															// argumento
	}

	// Testando o findall paginado
	@Test
	public void findAllShouldReturnPage() throws Exception {
		//andExpect = tem a mesma funcionalidade da assertion, faz a expectativa do resultado
		mockMvc.perform(get("/product")).andExpect(status().isOk()); //perform faz a requisição
	}
}
