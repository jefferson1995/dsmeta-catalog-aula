package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_product")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(columnDefinition = "TEXT")    // Porque a descrição é muito grande
	private String description;
	private Double price;
	private String imgUrl;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")  //amarzena o instant com data UTC
	private Instant date;
	
	// referencia tabela de exemplo que está no excel
	@ManyToMany   // Irá fazer o mapeamento da terceira tabela para fazer a relação
	   // @JoinTable   = É a tabela que faz a associação entre as duas entidades
	@JoinTable(name = "tb_product_category",
			joinColumns = @JoinColumn(name = "product_id"),   //pega da propria classe para referenciar o produto para fazer o mapeamento na terceira tabela tb_product_category
			inverseJoinColumns = @JoinColumn(name = "category_id"))   //vai referenciar a category do set(está na coleção) para fazer o mapeamento na terceira tabela tb_product_category
	
	Set<Category> categories = new HashSet<>();   //Assim como o list é uma interface (Não pode ser instanciada como Set) O produto conhece varias categorias // Set garante que não terá mais de uma categoria, não pode ter repetição;
	
	//Construtor sem argumentos
	public Product() {
		
	}
	//Construtor com argumentos (Não informa a coleção category)
	public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
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
	public Set<Category> getCategories() {
		return categories;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(id, other.id);
	}
	

	
}
