package com.devsuperior.dscatalog.services;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks // Usar quando a classe não carrega o contesto da aplicação
	private ProductService service;
	
	private long existingId;
	private long nonExistingId;
	
	//Vai ser executado antes de cada um dos testes
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		//Nesses exemplos é simulado tudo o que ocorre no repository
		
		//Mockito.when(null);   //Método usando quando precisa retornar alguma coisa
		
		Mockito.doNothing().when(repository).deleteById(existingId); //Para alguns casos que não retornam nada
		
		//Lança uma exceção quando passar o id que não existe.
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
	}
	//Quando cria um mock, precisa simular o comportamento dele.
	@Mock // Cria a classe de forma ilustrativa(de mentira) para fazer a simulação
	private ProductRepository repository;
	
	@Test
	public void deleteShouldIdDoNothingWhenIdExists() {
			// Nesse caso não vai disparar execeção quando eu chamar o id, porque ele não existe		
			Assertions.assertDoesNotThrow(() -> {
				service.delete(existingId);
			});
			
			//mockito time verifica quantas vezes o repository foi chamado. (never segnifica que nunca deve ser chamado)
			Mockito.verify(repository, Mockito.times(1)).deleteById(existingId); // verifica se o metodo deletebyid foi chamado. 
	}
	
}
