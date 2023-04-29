package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class ProductDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	//Existe estas anotações porque a requisição é feira no DTO -> depois precisa colocar o @Valid no resources
	@Size(min = 5, max =60, message = "Deve ter entre 5 e 60 caracteres") //Faz a validação e manda msg
	@NotBlank(message = "Campo obrigatório") //Tornar o campo obrigatório mesmo se colocar aspas vazia " "
	private String name;
	private String description;
	@Positive(message = "O preço deve ser um valor positivo") //Obriga a ser um valor positivo
	private Double price;
	private String imgUrl;
	
	@PastOrPresent(message = "A data do produto não pode ser futura ") //Bloqueia datas futuras
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	//Construtor sem argumentos
	public ProductDTO() {
		
	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	//Contrutor para receber a entidade
	public ProductDTO(Product entity) {
		super();
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
		entity.getCategories().forEach(cat -> this.categories.add(new CategoryDTO(cat)));
		 
	}
	
	//Construtor para receber as categorias (Sobrecarga mesmo contrutor mas com parametros difrentes)
	// Quando esse construtor for chamado irá colocar os elementos dentro da lista no inicio desse codigo
	
	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity); //Alimenta o construtor que tem o entity
		 // Para cada categoria nesse construtor, faz a função lambda para adicionar dentro da lista categories (No começo do codigo)
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}
	
	
	
}
