package org.paingan.zuul.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		 registry.addViewController("/login").setViewName("login");
//	     registry.addViewController("/").setViewName("index");
//	}
}
