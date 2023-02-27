package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

//Classe para atualizar usuário -> faz herança do userDTO, mas é possível inseir a senha

@UserUpdateValid  // anotation que irá verificar no B.D se o e-mail está sendo repitido(abotação foi criada)
public class UserUpdateDTO extends UserDTO{ /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//Herança do userDTO

	private String password;
	
	public UserUpdateDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
