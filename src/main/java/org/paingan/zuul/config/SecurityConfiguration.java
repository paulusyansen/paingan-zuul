package org.paingan.zuul.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@Order(-10)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/uaa/**","/login","/member/**","/order/**").permitAll()
//			.anyRequest().authenticated()
//			.and().formLogin()
//			.loginPage("/login")
			.and().httpBasic().disable();
	}

//	@Override
//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource);
//		auth.inMemoryAuthentication().withUser("admin").password("password").authorities("password");
//	}
	
//	 	@Bean
//	    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
//	        return new JwtTokenStore(jwtAccessTokenConverter);
//	    }

//	    @Bean
//	    public JwtAccessTokenConverter jwtAccessTokenConverter(OAuth2SignatureVerifierClient signatureVerifierClient) {
//	        return new OAuth2JwtAccessTokenConverter(oAuth2Properties, signatureVerifierClient);
//	    }

	    @Bean
		@Qualifier("loadBalancedRestTemplate")
	    public RestTemplate loadBalancedRestTemplate(RestTemplateCustomizer customizer) {
	        RestTemplate restTemplate = new RestTemplate();
	        customizer.customize(restTemplate);
	        return restTemplate;
	    }

	    @Bean
	    @Qualifier("vanillaRestTemplate")
	    public RestTemplate vanillaRestTemplate() {
	        return new RestTemplate();
	    }
}
