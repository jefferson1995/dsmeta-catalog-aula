package com.devsuperior.dscatalog.dto;


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
