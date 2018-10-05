package org.paingan.zuul.config;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;       
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.web.*;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	
//	@Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any()) 
          .build()
          .securitySchemes(Arrays.asList(securityScheme()))
          .securityContexts(Arrays.asList(securityContext()))
          .apiInfo(apiInfo())
          .useDefaultResponseMessages(false)                                   
          .globalResponseMessage(RequestMethod.GET,                     
            newArrayList(new ResponseMessageBuilder()   
              .code(500)
              .message("500 message")
              .responseModel(new ModelRef("Error"))
              .build(),
              new ResponseMessageBuilder() 
                .code(403)
                .message("Forbidden!")
                .build()));
    }

	private ApiInfo apiInfo() {
	     return new ApiInfo(
	       "Api Documentation", 
	       "[paingan-microservices]", 
	       "v1.0", 
	       "Terms of service", 
	       new Contact("Paulus Yansen", "http://www.github.com/paulusyansen", "paulus.yansen@gmail.com"), 
	       "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
	}
	
	private SecurityScheme securityScheme() {
	    GrantType grantType = new AuthorizationCodeGrantBuilder()
	        .tokenEndpoint(new TokenEndpoint("http://localhost:8700/uaa/oauth/token", "access_token"))
	        .tokenRequestEndpoint(
	          new TokenRequestEndpoint("http://localhost:8700/uaa/oauth/authorize", "paingan-client", "paingan-secret"))
	        .build();
	 
	    SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
	        .grantTypes(Arrays.asList(grantType))
	        .scopes(Arrays.asList(scopes()))
	        .build();
	    return oauth;
	}
	
	private SecurityContext securityContext() {
	    return SecurityContext.builder()
	      .securityReferences(
	        Arrays.asList(new SecurityReference("spring_oauth", scopes())))
	      .forPaths(PathSelectors.regex("/auth(/.*)?"))
	      .build();
	}
	
	private AuthorizationScope[] scopes() {
	    AuthorizationScope[] scopes = { 
	      new AuthorizationScope("read", "for read operations"), 
	      new AuthorizationScope("write", "for write operations"), 
	      new AuthorizationScope("trust", "Access API") };
	    return scopes;
	}
}
