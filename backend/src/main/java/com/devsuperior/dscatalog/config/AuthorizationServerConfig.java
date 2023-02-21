package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer //representa o Auth. server    //Checklist do OAuth 2.0
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter  {

	//OS QUATROS BEANS NECESSÁRIOS 
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtAccessTokenConverter acessTokenConverter;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	@Autowired //Está no websecurityconfig
	private AuthenticationManager authenticationManage;
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override  //Define como será a autenticação  -> app credentials 
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("dscatalog") //define o id da aplicação
		.secret(passwordEncoder.encode("dscatalog123")) //senha da aplicação
		.scopes("read", "write") //Para dizer que será um acesso de leitura e escrita
		.authorizedGrantTypes("password") //varios tipos de acesso login
		.accessTokenValiditySeconds(86400); //24 horas de validade
	}

	@Override //Informa quem vai autorizar e qual vai ser o formato do token
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManage)  //Processa a autenticação 
		.tokenStore(tokenStore)     //Objeto responsável para processar o token
		.accessTokenConverter(acessTokenConverter); //registra a chave
	}

	
	
}
