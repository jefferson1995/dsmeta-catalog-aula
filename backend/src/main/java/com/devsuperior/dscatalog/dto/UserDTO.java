package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.devsuperior.dscatalog.entities.User;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	@NotEmpty(message = "Campo obrigatório") //Não deixa o campo ser vazio
	private String firstName;
	private String lastName;
	
	@Email(message = "Favor entrar com um e-mail válido")  //Obriga a informar um e-mail
	private String email;
	
	//Para transitar os dados do usuário e as permissões dele
	Set<RoleDTO> roles = new HashSet<>();
	
	
	public UserDTO() {
		
	}
	
	//Não será necessário ter a senha, porque será aplicada em um DTO separado 

	public UserDTO(long id, String firstName, String lastName, String email, String password) {
		
		//This serve apenas para referências os paramêtros
		
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		
	}
	
	//Construtor para adicionar os dados da classe entity no DTO
	public UserDTO(User entity) {
	
		//Não precisa usar o this, porque o paramêtro é diferente
		
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		//Pega a lista de role que já veio com o usuário(do banco por padrão) e insere na lista de roles
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
		
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//Somente o getRoles para buscar as permissões vinculadas 
	public Set<RoleDTO> getRoles() {
		return roles;
	}
	
	
	
	
	
	
}
