package com.devsuperior.dscatalog.resources;

import java.net.URI;

import javax.validation.Valid;

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

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	@Autowired
	private UserService service;

	/*
	 * desativado para usar o Pageable que faz o mesmo procedimento, porém, código
	 * mais limpo
	 * 
	 * @GetMapping public ResponseEntity<Page<UserDTO>> findAll( // parametros
	 * para paginar a requisição
	 * 
	 * @RequestParam(value = "page", defaultValue = "0") Integer page,
	 * 
	 * @RequestParam(value = "linesPerPage", defaultValue = "12") Integer
	 * linesPerPage,
	 * 
	 * @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
	 * 
	 * @RequestParam(value = "direction", defaultValue = "ASC") String direction
	 * 
	 * ) {
	 */

	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {

		// Parametros: page, size, sort

		Page<UserDTO> list = service.findAllPaged(pageable);

		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {

		UserDTO dto = service.findById(id);

		return ResponseEntity.ok().body(dto);
	}

	@PostMapping                             //valid para atender as anotações do DTO
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {
		UserDTO newDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDTO.getId()).toUri();

		return ResponseEntity.created(uri).body(newDTO);
	}

	@PutMapping(value = "/{id}") // método indepotente
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}") // método indepotente
	public ResponseEntity<UserDTO> delete(@PathVariable Long id) {
		service.delete(id);

		return ResponseEntity.noContent().build();
	}
}

//GetMapping = Configura o endpoint

/*
 * ResponseEntity encapsula uma resposta http -> A frente -> a lista é o tipo de
 * dado que vai encapsular FindAll = Busca todas categorias dentro da classe
 * User Na sequencia é instaciada a classe User e colocado dentro da lista
 * .ok = deixa responder o status 200 que significa que a solicitação foi
 * realizada com sucesso body = define o corpo da resposta. return = retorna os
 * dados em json (retorna a lista)
 * 
 * 
 */