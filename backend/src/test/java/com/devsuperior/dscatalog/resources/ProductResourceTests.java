package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class) // Carrega somente o contexto da classe principal
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean // carrega os contexto parcialmente, mas substitui por um componente mockado
	private ProductService service;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page; // retorna uma pagina de productDTO
	private Long existingId;
	private Long nonExistingId;

	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;
		nonExistingId = 2L;
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO)); // nova lista de productDTO

		//Simulando um retorno paginado.
		when(service.findAllPaged(any())).thenReturn(page); // any = vem do argumentMatchers - significa qualquer
															// argumento
		
		//Simulando a busca quando existe um id
		when(service.findById(existingId)).thenReturn(productDTO);
		//Simulando quando não existe um id e lança um exceção
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		
	}

	// Testando o findall paginado
	@Test
	public void findAllShouldReturnPage() throws Exception {
		//andExpect = tem a mesma funcionalidade da assertion, faz a expectativa do resultado
		
		ResultActions result = mockMvc.perform(get("/products"));//retorna o resultado //perform faz a requisição
				//.accept(MediaType.APPLICATION_JSON)); //Informa que aceita aceita como resposta o tipo JSON
				
		result.andExpect(status().isOk()); // faz a assetions para comparar se o resultado foi verdadeiro
	}
	
	//Teste para buscar o produto por id
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		//Passa como parametro o id existente
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON)); 
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists()); // Acessa o json corpo da resposta retornado e verifica se existe o id
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	//teste para quando realizar a busca e não existir um id, retorna um exceção
	@Test
	public void findByIdShouldReturnNotFoundWhenIdnonExists()throws Exception {
		//Passa como parametro o id existente
			ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON)); 
			
			result.andExpect(status().isNotFound());
	}
}
