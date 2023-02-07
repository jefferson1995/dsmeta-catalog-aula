package com.devsuperior.dscatalog.resources;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

//Relamente acessa a classe service e verifica toda integração da aplicação com o banco de dados.

@SpringBootTest
@Transactional // Faz um rollback no banco de dados a cada teste realizado.
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

	// Testa quando a página não existe
	@Test
	public void findAllPagedReturnSortedOrderedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name")); // Retorna uma página ordenada por nome
		Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty()); // Verifique se a página não está vazia
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName()); // Verifica se é o primeiro produto ordenado
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName()); // Verfica o segunto
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());// terceiro

	}

	// Testa quando a página não existe
	@Test
	public void findAllPagedReturnEmptyPageDoesNotExists() {
		PageRequest pageRequest = PageRequest.of(50, 10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertTrue(result.isEmpty()); // Verifique se a página não está vazia

	}

	// Realize o teste para verificar se está retornando uma página
	@Test
	public void findAllPagedReturnPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty()); // Verifique se a página não está vazia
		Assertions.assertEquals(0, result.getNumber()); // Verifica se a página é igual a zero
		Assertions.assertEquals(10, result.getSize()); // Verifica se o total de páginas é 10
		Assertions.assertEquals(countTotalProducts, result.getTotalElements()); // Verifica se tem a mesma quantidade de
																				// itens(produtos)
	}

	// Lança um exceção quando o id não existe no banco de dados
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	// Teste para verificar se está deletando o id do banco
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {

		service.delete(existingId);

		Assertions.assertEquals(countTotalProducts - 1, repository.count()); // Acessa o banco de dados e verifica
																				// quantos itens tem para validar
	}

}
