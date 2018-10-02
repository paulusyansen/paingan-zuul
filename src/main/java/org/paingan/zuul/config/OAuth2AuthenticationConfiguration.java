package org.paingan.zuul.config;

import org.paingan.zuul.service.CookieTokenExtractor;
import org.paingan.zuul.service.OAuth2AuthenticationService;
import org.paingan.zuul.service.OAuth2CookieHelper;
import org.paingan.zuul.service.OAuth2TokenEndpointClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;

/**
 * Configures the RefreshFilter refreshing expired OAuth2 token Cookies.
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = false)
public class OAuth2AuthenticationConfiguration extends ResourceServerConfigurerAdapter {
    private final OAuth2TokenEndpointClient tokenEndpointClient;
//    private final TokenStore tokenStore;

    public OAuth2AuthenticationConfiguration(OAuth2TokenEndpointClient tokenEndpointClient) {
        this.tokenEndpointClient = tokenEndpointClient;
//        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/auth/login").permitAll()
            .antMatchers("/auth/refresh").permitAll()
            .antMatchers("/auth/logout").authenticated();
//            .and()
//            .apply(refreshTokenSecurityConfigurerAdapter());
    }

    /**
//     * A SecurityConfigurerAdapter to install a servlet filter that refreshes OAuth2 tokens.
//     */
//    private RefreshTokenFilterConfigurer refreshTokenSecurityConfigurerAdapter() {
//        return new RefreshTokenFilterConfigurer(uaaAuthenticationService(), tokenStore);
//    }

    @Bean
    public OAuth2CookieHelper cookieHelper() {
        return new OAuth2CookieHelper();
    }

    @Bean
    public OAuth2AuthenticationService uaaAuthenticationService() {
        return new OAuth2AuthenticationService(tokenEndpointClient, cookieHelper());
    }

    /**
     * Configure the ResourceServer security by installing a new TokenExtractor.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenExtractor(tokenExtractor());
    }

    /**
     * The new TokenExtractor can extract tokens from Cookies and Authorization headers.
     *
     * @return the CookieTokenExtractor bean.
     */
    @Bean
    public TokenExtractor tokenExtractor() {
        return new CookieTokenExtractor();
    }

}
