package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.util.Objects;

//Serializable: Padrão da linguagem java para que o objeto java possa ser convertido em byte, para o objeto ser gravado em arquivos e passar nas redes.) 
public class Category implements Serializable {      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public Category() {

	}

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

	//Método padrão que todo objeto pode ter em java, para comparar se um objeto é igual a outro (Compara se os números são iguais).
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	//Método de comparação que qualquer objeto java pode ter, precisão de comparação 100%.
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
