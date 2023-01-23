package com.devsuperior.dscatalog.repositories; //Mesmo nome da classe para caso tenha alguma private/protected é possível acessar

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsuperior.dscatalog.entities.Product;

@DataJpaTest  // Carrega somente os componentes relacionados ao Spring Data JPA. 
public class ProductRepositoryTests {

	//Teste para verificar se está deletando. 
	
	@Autowired
	private ProductRepository repository;
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		long exintingId = 1L;
		
		repository.deleteById(exintingId);
		
		// Recebe o resultado para verificar se o ID ainda existe
		Optional<Product> result = repository.findById(exintingId);
		
		Assertions.assertFalse(result.isPresent()); //Verifica se não está presente o ID
	}
	
}
