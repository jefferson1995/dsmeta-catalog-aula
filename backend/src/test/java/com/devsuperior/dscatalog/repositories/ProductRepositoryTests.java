package com.devsuperior.dscatalog.repositories; //Mesmo nome da classe para caso tenha alguma private/protected é possível acessar

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest  // Carrega somente os componentes relacionados ao Spring Data JPA. 
public class ProductRepositoryTests {

	//Teste para verificar se está deletando. 
	
	@Autowired
	private ProductRepository repository;
	
	private long exintingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	//Vai ser executado antes de cada um dos testes
	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		//Não precisa ficar repetindo
	}
	
	//Teste para quando salva com o id nullo e cria um ID automatico
	@Test
	public void salveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct(); // Produco foi criado dentro da classe factory
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId()); // Verifica se o id não é null, ou seja ele fez o auto increment
		
		//Teste para verificar se criou o proximo id automatico 26
		
		Assertions.assertEquals(countTotalProducts + 1, product.getId()); // Verifica se o id é 26
		
	}
	
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		
		repository.deleteById(exintingId);
		
		// Recebe o resultado para verificar se o ID ainda existe
		Optional<Product> result = repository.findById(exintingId);
		
		Assertions.assertFalse(result.isPresent()); //Verifica se não está presente o ID
	}
	
	
	//Outro cenario para verificar se é disparada a exceção quando não existe ID.
	
	@Test
	public void deleteShouldThrowmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
	
	//retorna opção não vazio quando o id existir
	@Test
	public void findByIdreturnsNonEmptyOptionWhhenIdExists() {
		Optional<Product> result = repository.findById(exintingId); // retorna a classe optional product
		
		Assertions.assertTrue(result.isPresent()); // Verifica se a classe que retornou está presente
	}
	//retorna opção vazio quando o id não existir
	@Test
	public void FinfByIdreturnsEmptyOptionWhenIdDoesNotExist(){
		
		Optional<Product> result = repository.findById(nonExistingId);
	
		Assertions.assertTrue(result.isEmpty()); //Empty verifica se o retorno Optional product está vazio 
	
	}
	
}
