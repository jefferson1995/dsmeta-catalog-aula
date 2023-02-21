package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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

	@Autowired
	private JwtTokenStore tokenStore; //Pega o token
	
	//endpoints publicos - pode adicionar mais rotas caso necessário
	
	private static final String[] PUBLIC = {"/oauth/token"}; //Precisa ser publico pro usuário logar
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
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll() //Com este perfil está liberado para todo mundo
		
		.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll() //Libera para todo mundo somente no método get
		
		//No banco é preciso cadastrar "role_admin" mas nessa linha basta passar o "operator que é reconhecido o perfil"
		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN") //Permite acessar somente quem tem o perfil (role) operator e admin acessa: put/delete/post
		
		.antMatchers(ADMIN).hasAnyRole("ADMIN") //Somente quem tem o perfil admin - acessa: put/delete/post
		
		.anyRequest().authenticated(); //Quem for acessar qualquer outra rota, precisa estar logado
	}
	
	//antMatchers define as autorizações 

	
}
