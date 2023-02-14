package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//classe responsável por alguma configuração ou componente especifico do projeto

@Configuration
public class AppConfig {

	@Bean  //Componente do spring para métodos, posso injetar em outras classes
	//Método para gerar o código de senha do usuário, transforma senha em código
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
