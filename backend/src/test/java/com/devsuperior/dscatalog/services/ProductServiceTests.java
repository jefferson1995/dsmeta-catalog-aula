package com.devsuperior.dscatalog.services;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks // Usar quando a classe não carrega o contesto da aplicação
	private ProductService service;
	
	@Mock // Cria a classe de forma ilustrativa(de mentira) para fazer a simulação
	private ProductRepository repository;
	
}
