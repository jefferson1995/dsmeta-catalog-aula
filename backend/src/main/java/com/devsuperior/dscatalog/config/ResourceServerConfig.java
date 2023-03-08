package com.devsuperior.dscatalog.config;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//Classe que verifica a requisição e token e diz se pode entregar o recurso

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired //ambiente de execuçaõ da aplicação
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore; //Pega o token
	
	//endpoints publicos - pode adicionar mais rotas caso necessário
	
	private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**"}; //Precisa ser publico pro usuário logar
	//Rotas liberadas para adimin e operador 
	private static final String[] OPERATOR_OR_ADMIN = {"/products/**", "/categories/**"}; //**indica tudo o que vem depois
	
	private static final String[] ADMIN = {"/users/**"};
	
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		//Verifica se o token é valido / ou está inválido
		resources.tokenStore(tokenStore); //Configuração para trabalhar com token 
	}

	//Usado para configurar as rotas
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//Configuração para liberar o H2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable(); //Para liberar as opções do H2
		}
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll() //Com este perfil está liberado para todo mundo
		//Libera para todo mundo somente no método get -  
		.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll() // <- operador_or_admin -> libera todas permissões get/put/delete
		
		//No banco é preciso cadastrar "role_admin" mas nessa linha basta passar o "operator que é reconhecido o perfil"
		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN") //Permite acessar somente quem tem o perfil (role) operator e admin acessa: put/delete/post
		
		.antMatchers(ADMIN).hasAnyRole("ADMIN") //Somente quem tem o perfil admin - acessa: put/delete/post
		
		.anyRequest().authenticated(); //Quem for acessar qualquer outra rota, precisa estar logado
	}
	
	//antMatchers define as autorizações 

	
}
