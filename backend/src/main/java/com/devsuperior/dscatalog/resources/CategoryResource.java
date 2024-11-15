package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

// Controladores REST
/* RestControlller = Configuração de anotação para usar algo que já está implementado (Efetua um
pré processamento ao compilar esta classe) economiza tempo. 
*/

// ResquestMapping = É a rota rest do meu recurso (endereço)

@RestController
@RequestMapping(value = "/categories") // colocar no padrão plural (rota)
public class CategoryResource { // Esta é uma classe que pertence o recurso da entidade.

	@Autowired // para injetar o mecanismo automaticamente
	private CategoryService service;

	// Endpoint
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable){
		
		/*
		 * List<Category> list = new ArrayList<>();
		 * 
		 * list.add(new Category(1L, "Books")); list.add(new Category(2L,
		 * "Electonics"));
		 */

		// troca pela lista do banco de dados

		//List<CategoryDTO> list = service.findAll(); Desativado para usar o page
		
		
		//Parametros: page, size, sort	
		
		Page<CategoryDTO> list = service.findAllPaged(pageable);

		return ResponseEntity.ok().body(list);
	}
	
	
	

	//Método para retornar consulta quando informa o id
	
	@GetMapping(value = "/{id}")  // Esse id irá ser adicionado na rota básica "/categories"
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) { // Não irá mais retornar uma lista (@PathVariable) para reconhecer o id (Passa na url o parametro) usando quando obrigatório
		
		CategoryDTO dto = service.findById(id);

		return ResponseEntity.ok().body(dto);
	}
	
	
	
	
	//Método para Inserir novos dados
	
	@PostMapping      // Anotação porque no padrão REST usamos o post para inserir dados
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		dto = service.insert(dto);
		// Código com o endereço dos dados que estão sendo adicionado, nesse caso o id
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	
	
	
	//Método para atualizar os dados
	
	@PutMapping(value = "/{id}")  //método indepotente
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	
	
	//Método para deletar os dados
	
	@DeleteMapping(value = "/{id}")  //método indepotente
	public ResponseEntity<CategoryDTO> delete(@PathVariable Long id){
		service.delete(id);
	
		return ResponseEntity.noContent().build();    // Não é preciso passar um body, retorna um 204 informando que foi concluido(não em corpo na resposta). 
	}
}


//GetMapping = Configura o endpoint

/*
 * ResponseEntity encapsula uma resposta http -> A frente -> a lista é o tipo de
 * dado que vai encapsular FindAll = Busca todas categorias dentro da classe
 * Category Na sequencia é instaciada a classe Category e colocado dentro da
 * lista .ok = deixa responder o status 200 que significa que a solicitação foi
 * realizada com sucesso body = define o corpo da resposta. return = retorna os
 * dados em json (retorna a lista)
 * 
 * 
 */