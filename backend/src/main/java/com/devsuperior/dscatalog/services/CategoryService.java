package com.devsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

/* Service = essa anotação vai registrar a classe como um componente que vai participar do sistema de injeção
 * de dependencia automaziada do spring, ou seja, faz o gerenciamento das instancias das dependências dos objetos CategoryService
 * vai ser o spring, ele faz o genrenciamento 
 * @Component = para componentes genericos
 * @Repository = se for um repositório 
 * @Service = para serviços
 * 
 */

@Service
public class CategoryService {
	
	@Autowired    
	private CategoryRepository repository;   // Com essas configurações o spring trata de injetar uma dependencia valida para CategoryRepository
	
	public List<Category> findAll(){
		return repository.findAll();
	}
}
