package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// tem os mesmos nomes da classe Category
	private Long id;
	private String name;

	public CategoryDTO() {

	}

	public CategoryDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/*
	 * construtor que recebe a entidade irá reconhecer os dados pelo fato de ter
	 * passado como parametro o (Category entity)
	 */
	public CategoryDTO(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
