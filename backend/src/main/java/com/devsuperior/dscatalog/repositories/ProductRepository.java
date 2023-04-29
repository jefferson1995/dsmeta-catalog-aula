package com.devsuperior.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

// CAMADA DE ACESSO A DADOS
//Camada chamada de acesso a dados

// JPAREPOSITORY É UM TIPO GENERICO E É PRECISO PASSAR DOIS PARAMENTROS tipo da entidade e o id
//Existe varias operações prontas para acessar o banco de dados e funciona para qualquer banco relacional que tem relação com o JPA

// @Repository = para registrar essa classe como um componente injetavel pelo mecanismo do spring ( Passa a ser gerenciado pelo spring)

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	//JPQL
	@Query("SELECT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
			+ ":category IN cats")
	Page<Product> find(Category category, Pageable pageable);
	
}
