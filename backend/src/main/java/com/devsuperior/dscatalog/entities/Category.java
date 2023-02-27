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
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
// CAMADA ENTIDADE

//Serializable: Padrão da linguagem java para que o objeto java possa ser convertido em byte, 
//para o objeto ser gravado em arquivos e passar nas redes.) 

/* Entity =
 *  é utilizada para informar que uma classe também é uma entidade. A partir disso,
 *   a JPA estabelecerá a ligação entre a entidade e uma tabela de mesmo nome no banco de dados,
 *   onde os dados de objetos desse tipo poderão ser persistidos. 
 */
// Table = marcação para criar o nome da tabela.



@Entity
@Table(name = "tb_category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)     //  está marcação implementa o id automaticamente       //
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")  // adiciona no banco o momento exato no UTC
	private Instant createdAt;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") 
	private Instant updateAt;

	//Jpa vai no banco e busca os produtos associados
	@ManyToMany(mappedBy = "categories" ) //Faz o mapeamento com base no que já está na classe Product 
	private Set<Product> products = new HashSet<>();
	
	//construtor sem argumentos
	public Category() {

	}
	
	
   //Construtor com argumentos
	public Category(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
		
		
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
	
	

	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public Instant getUpdateAt() {
		return updateAt;
	}
	
	//Método para atualizar juntos o updateAt e createdAt - anotação @PrePersist para executar a ação
	
	@PrePersist    // anotação h2
	public void prePersist() {
		createdAt = Instant.now();
	}
	@PreUpdate
	public void preUpdate() {
		updateAt = Instant.now();
	}
	
	
	
	
	// Método padrão que todo objeto pode ter em java, para comparar se um objeto é
	// igual a outro (Compara se os números são iguais).
	
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	
	//Método para buscar os produtos que estão associados a esta classe Category
	public Set<Product> getProducts() {
		return products;
	}
	
	// Método de comparação que qualquer objeto java pode ter, precisão de
		// comparação 100%.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id);
	}


	

}
