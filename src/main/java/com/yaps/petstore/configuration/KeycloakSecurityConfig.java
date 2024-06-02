package com.yaps.petstore.configuration;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@EnableGlobalMethodSecurity(securedEnabled = true) // permettra @Secured ({"ROLE_USER"}) ...
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakWebSecurityConfigurerAdapter.class);

	// Submits the KeycloakAuthenticationProvider to the AuthenticationManager
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
          new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	final String mname = "configure";
		LOGGER.debug("entering "+mname);
		
		super.configure(http); 
		
		String[] staticResources = { "/img/**"};
    	 
        http
        	.authorizeRequests()
        	.antMatchers(staticResources).permitAll()
        	.antMatchers("/","/new-account","/find-products","/find-items","/find-item").permitAll()
        	.antMatchers(HttpMethod.GET,"/api/**").permitAll()
        	.antMatchers(HttpMethod.POST,"/api/**").hasRole("ADMIN")			// et enlever des RestControllers => test en 403 au lieu de 200
        	.antMatchers(HttpMethod.PUT,"/api/**").hasRole("ADMIN")
        	.antMatchers(HttpMethod.DELETE,"/api/**").hasRole("ADMIN")
        	.anyRequest().authenticated();
    }    
}