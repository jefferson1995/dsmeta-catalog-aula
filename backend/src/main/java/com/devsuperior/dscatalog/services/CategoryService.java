package com.devsuperior.dscatalog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
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
	
	@Transactional(readOnly= true)    // tem a garantia da transação, se faz tudo ou não faz nada, evita lock no banco e melhora a performance (readOnly)
	public List<CategoryDTO> findAll(){  
		List<Category> list =  repository.findAll();  // acessa o repositório e guarda todas informações dentro da lista do tipo Category
		
		/*
		 * criado uma nova lista para guardar os dados
		 * For each para percorrer a lista category e adicionar dentro da nova lista dto e já passar como parametro
		 * 
		 */
		
		/* usando o stream e map para criar  a nova lista
		 * stream ( recurso para trabalhar com expressao lambda) 
		 * map = transforma cada elemento original em outra coisa, aplica a funcão a cada elemento da lista
		 */
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList()); // transforma a list de Category para uma lista CategoryDTO
		// no final o collect transforma o stream para lista novamente.
		
		/* alternativa para utilizar
		 * 
		List<CategoryDTO> listdto = new ArrayList<>();
		for (Category cat : list) {
			listdto.add(new CategoryDTO(cat));
		}
		*/
		
	}
}