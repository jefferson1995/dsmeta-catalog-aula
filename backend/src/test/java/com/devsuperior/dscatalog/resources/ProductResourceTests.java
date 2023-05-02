package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

//@WebMvcTest(ProductResource.class) // Carrega somente o contexto da classe principal

//alterado para carregar o contexto devido as mudanças na aplicação
@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean // carrega os contexto parcialmente, mas substitui por um componente mockado
	private ProductService service;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page; // retorna uma pagina de productDTO
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Long createID;
	private String username;
	private String password;
	
	
	@Autowired
	private ObjectMapper objectMapper; //Para converter em JSON
	
	@Autowired
	private TokenUtil tokenUtil;

	@BeforeEach
	void setUp() throws Exception {

		username = "maria@gmail.com";
		password = "123456";
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		createID = 26L;
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO)); // nova lista de productDTO

		//Simulando um retorno paginado.
		when(service.find(any(), any(), any())).thenReturn(page); // any = vem do argumentMatchers - significa qualquer
															// argumento - simula qualquer objeto
		
		//Simulando a busca quando existe um id
		when(service.findById(existingId)).thenReturn(productDTO);
		//Simulando quando não existe um id e lança um exceção
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		//Simulando um update
		when(service.update(eq(existingId), any())).thenReturn(productDTO); //eq - Método para não ser um objeto simples
		//Simulando quando não existe um id e lança um exceção
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		//Simulando o deleteID
		doNothing().when(service).delete(existingId);
		// quando não existe id~
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		//Quando tem um objeto associado que viola a integridade
		doThrow(DatabaseException.class).when(service).delete(dependentId);
		
		//Simulando a criação de um objeto
		when(service.insert(any())).thenReturn(productDTO);
		
	}
	
	@Test
	public void deleteIDShouldReturnNoContentWhenIDExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	
	public void deleteIDShouldReturnNotFoundWhenIDExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void InsertShouldReturnProductDTOCreated() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody) //tipo corpo da requisição
				.contentType(MediaType.APPLICATION_JSON) //Tipo de requisição
				.accept(MediaType.APPLICATION_JSON)); 
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}

	
	//Faz o update quando o ID existe
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO); //Realizar a conversão para uma String JSON
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody) //tipo corpo da requisição
				.contentType(MediaType.APPLICATION_JSON) //Tipo de requisição
				.accept(MediaType.APPLICATION_JSON)); 
		
				//Verifica se estes campos realmente existe após a requisição
				result.andExpect(status().isOk());
				result.andExpect(jsonPath("$.id").exists());
				result.andExpect(jsonPath("$.name").exists());
				result.andExpect(jsonPath("$.description").exists());
		
	}
	
	
	//Quando o ID não existe
	@Test
	public void updateShouldReturnProductDTOWhenIdDoesNotExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
				
		result.andExpect(status().isNotFound());
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
