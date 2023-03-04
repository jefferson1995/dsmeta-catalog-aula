package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;

//Classe para inserir um novo usuário -> faz herança do userDTO, mas é possível inseir a senha

@UserInsertValid  // anotation que irá verificar no B.D se o e-mail está sendo repitido(anotação foi criada)
public class UserInsertDTO extends UserDTO{ //Herança do userDTO

	private String password;
	
	public UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
