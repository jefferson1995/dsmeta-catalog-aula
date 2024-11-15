package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

// CAMADA DE SERVIÇO

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
	public Page<CategoryDTO> findAllPaged(Pageable pageable){  
		//List<Category> list =  repository.findAll();  // acessa o repositório e guarda todas informações dentro da lista do tipo Category (Desativado para usar o page)
		
		Page<Category> list =  repository.findAll(pageable);  // faz a busca paginada
		/*
		 * criado uma nova lista para guardar os dados
		 * For each para percorrer a lista category e adicionar dentro da nova lista dto e já passar como parametro
		 * 
		 */
		
		/* usando o stream e map para criar  a nova lista
		 * stream ( recurso para trabalhar com expressao lambda) 
		 * map = transforma cada elemento original em outra coisa, aplica a funcão a cada elemento da lista
		 */
		
		//return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList()); // transforma a list de Category para uma lista CategoryDTO (desativado par ausar o page)
		// no final o collect transforma o stream para lista novamente.
		
		return list.map(x -> new CategoryDTO(x));  // instancia a lista de page para CategoryDTO
		
		/* alternativa para utilizar
		 * 
		List<CategoryDTO> listdto = new ArrayList<>();
		for (Category cat : list) {
			listdto.add(new CategoryDTO(cat));
		}
		*/
		
	}

	//Método para buscar os dados
	@Transactional(readOnly= true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);  // Optional é uma abordagem para evitar trabalhar com o valor nulo
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Nenhum id encontrado"));  //Retorna o objeto que estava dentro do option
		// orElse procura o objeto, mas se não encontrar ele dispara a exception
		return new CategoryDTO(entity);
	}
	
	
	
	//Método para salvar uma nova categoria no banco de dados
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		
		Category entity = new Category();  // instancia a categoria 
		entity.setName(dto.getName());    // inseri o nome e a categoryDTO pega o nome que foi inserido
		entity = repository.save(entity);   // Precisa retornar para classe Category o que foi adicionado. 
		return new CategoryDTO(entity);  // retorna uma CategoryDTO passado como parametro a nova classe Category 
	}

	
	//Método para atualizar os dados
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity = repository.getOne(id);  // Ele não toca no banco de dados, instacia um objeto provisório com os dados e o id, quando clica em salvar ele toca o banco de dados 
		entity.setName(dto.getName());
		entity = repository.save(entity);
		
		return new CategoryDTO(entity);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("id não encontrado" + id);
		}
	
	}

	//método para excluir os dados
	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado" + id);
		}
		catch(DataIntegrityViolationException e) { //Lança uma exceção quando tenta apagar uma classe que tem relação com um chavae estrangeira, ela não pode ficar vazia
			throw new DatabaseException("Violação de integridade");
		}
	} 
}
