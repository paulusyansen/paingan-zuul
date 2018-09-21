package org.paingan.zuul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(-10)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http
//		.authorizeRequests()
//			.antMatchers("/login").permitAll()
//			.anyRequest().authenticated()
//			.and().formLogin()
//			.loginPage("/login")
//			.defaultSuccessUrl("/")
//			.and().httpBasic().disable();
		
		http.authorizeRequests().anyRequest().permitAll();
		
//		http
//		.authorizeRequests()
//			.antMatchers("/uaa/**","/login").permitAll()
//			.anyRequest().authenticated()
//			.and().formLogin()
//			.loginPage("/login")
//			.and().httpBasic().disable();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource);
		auth.inMemoryAuthentication().withUser("root").password("password").roles("USER");
	}
}
