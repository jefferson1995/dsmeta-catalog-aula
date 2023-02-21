package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//classe responsável por alguma configuração ou componente especifico do projeto

@Configuration
public class AppConfig {

	@Bean  //Componente do spring para métodos, posso injetar em outras classes
	//Método para gerar o código de senha do usuário, transforma senha em código
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//São dois objetos capazer de acessar um token jwt -> codifica -> cria  e faz a leitura
	//depois são implementadas no authorization server
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("MY-JWT-SECRET"); //Segredo do token -> registra a chave 
		return tokenConverter;
	}

	//Para acessar o token também 
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}
