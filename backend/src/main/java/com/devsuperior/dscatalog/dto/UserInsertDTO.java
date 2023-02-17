package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;

@UserInsertValid  // anotation que irá verificar no B.D se o e-mail está sendo repitido(abotação foi criada)
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
