package com.devsuperior.dscatalog.resources;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

//Relamente acessa a classe service e verifica toda integração da aplicação com o banco de dados.

@SpringBootTest
public class ProductServiceIT {

	@Autowired
	private ProductService service;
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	
	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	//Lança um exceção quando o id não existe no banco de dados
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
			
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(existingId);
		});
	}
	
	//Teste para verificar se está deletando o id do banco
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts -1, repository.count()); // Acessa o banco de dados e verifica quantos itens tem para validar
	}
	
	
}
