package com.devsuperior.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.entities.Category;

/* RestControlller = Configuração de anotação para usar algo que já está implementado (Efetua um
pré processamento ao compilar esta classe) economiza tempo. 
*/

// ResquestMapping = É a rota rest do meu recurso (endereço)

@RestController
@RequestMapping(value = "/categories") // colocar no padrão plural (rota)
public class CategoryResource { // Esta é uma classe que pertence o recurso da entidade.

	//	Endpoint

	@GetMapping    //Configura o endpoint
	public ResponseEntity<List<Category>> findAll() { 
		List<Category> list = new ArrayList<>();
		list.add(new Category(1L, "Books"));
		list.add(new Category(2L, "Electonics"));

		return ResponseEntity.ok().body(list);
	}
}


//GetMapping = Configura o endpoint

/*
	ResponseEntity encapsula uma resposta http  -> A frente ->  a lista é o tipo de dado que vai encapsular 
	FindAll = Busca todas categorias dentro da classe Category
	Na sequencia é instaciada a classe Category e colocado dentro da lista
	.ok = deixa responder o status 200 que significa que a solicitação foi realizada com sucesso
	body = define o corpo da resposta.
	return = retorna os dados em json (retorna a lista)
	

*/