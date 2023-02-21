package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Classe criada para liberar todos endpoints, para não ter que fazer autenticação


@Configuration
@EnableWebSecurity						//checklist do OAuth 2.0
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// As duas classes para verificar na configuração
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	
	@Override 		
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Está subscrevendo o AuthenticationManagerBuilder
		//Utilizado para informar qual o algoritmo para criptografar a senha BCryptPasswordEncoder
		//Configura quem é o userDetailsService
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); //consegue verificar onde
		//vai buscar o usuário por e-mail e como vai analisar a senha criptografada
		super.configure(auth);
	}

	//Método para liberar os endpoints -> está subscrevendo o o WebSecurity
	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers("/**"); desativado para usar do OAuth 2.0
		web.ignoring().antMatchers("/actuator**"); //Método para usar o OAuth 2.0
	}

	//checklist do OAuth 2.0
	@Override
	@Bean //na hora que for implementar o authorization server precisa do objeto authenticationManager
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	
	
}