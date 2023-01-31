package com.devsuperior.dscatalog.services;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks // Usar quando a classe não carrega o contesto da aplicação
	private ProductService service;
	
	//Quando cria um mock, precisa simular o comportamento dele.
		@Mock // Cria a classe de forma ilustrativa(de mentira) para fazer a simulação
		private ProductRepository repository;
		
		//simulando o comportamento do Category Repository
		@Mock
		private CategoryRepository categoryRepository;
		
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page; // Tipo concreto que representa uma pagina de dados
	ProductDTO productDTO;
	// produto instaciando para simular o comportamento do findall
	
	private Category category;
	
	
	//Vai ser executado antes de cada um dos testes
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		Product product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		
		
		//Nesses exemplos é simulado tudo o que ocorre no repository
		
		
		//Método usando quando precisa retornar alguma coisa
		
		/*Chama o findall
		 * passa o Argument (um objeto qualquer do tipo pageable)
		 * No final retorna um page (vai ser um objeto de pagina "PageImpl e uma lista dentro com apenas um produto"
		 */
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		
		
		//Smulando como salvar uma novo objeto
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product); //Nesse caso retorna um produto
		
		//Simulando como atualizar um novo objeto
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		//Simulando como atualizar uma categoria do produto
				Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
				Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		//Simulando quando busca por um id
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product)); //Quando existe id
		Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class); //quando não existe um id
			
		
	
		
		
		//casos que não retornam nada
		Mockito.doNothing().when(repository).deleteById(existingId); 
		
		//Lança uma exceção quando passar o id que não existe.
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		
		//Quando chama um id que tem outra entidade que depende dele (chave estrangeira não pode ficar vazia)
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		
		
		
	}
	
	//Quando vai atualizar , mas não retorna nenhum id
	
	@Test 
	public void updateShoulResourceNotFoundExceptionWhenIdDoesNotExists() {
			// Nesse caso vai dispara quando chamar o id, porque ele não existe		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				service.update(nonExistingId, productDTO);
			});
	}
	
	
	
	
	//Atualiza o objeto quando existir um id
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
	}
	
	
	//Lança uma exception quando o id não existe
	@Test 
	public void findByIdShoulResourceNotFoundExceptionWhenIdDoesNotExists() {
			// Nesse caso vai dispara quando chamar o id, porque ele não existe		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				service.findById(nonExistingId);
			});
	}
	
	//Retorna um produto DTO quando o id existir. 
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {

		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
			
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
		
	}
	


	//Teste quando viola a integridade
	@Test
	public void deleteShoulDatabaseExceptionWhenDependentId() {	
			//Dispara a exceção porque foi violada a integridade
			Assertions.assertThrows(DatabaseException.class,() -> {
				service.delete(dependentId);
			});
			
			//mockito time verifica quantas vezes o repository foi chamado. (never segnifica que nunca deve ser chamado)
			Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId); // verifica se o metodo deletebyid foi chamado. 
	}
	
	
	
	
	
	
	//Teste quando tenta excluir um id que não existe
	@Test
	public void deleteShoulResourceNotFoundExceptionWhenIdDoesNotExists() {
			// Nesse caso vai dispara quando chamar o id, porque ele não existe		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				service.delete(nonExistingId);
			});
			
			//mockito time verifica quantas vezes o repository foi chamado. (never segnifica que nunca deve ser chamado)
			Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId); // verifica se o metodo deletebyid foi chamado. 
	}
	
	
	
	//teste para excluir um id
	@Test
	public void deleteShouldIdDoNothingWhenIdExists() {		
			Assertions.assertDoesNotThrow(() -> {
				service.delete(existingId);
			});
			
			//mockito time verifica quantas vezes o repository foi chamado. (never segnifica que nunca deve ser chamado)
			Mockito.verify(repository, Mockito.times(1)).deleteById(existingId); // verifica se o metodo deletebyid foi chamado. 
	}
	
	
	
}
