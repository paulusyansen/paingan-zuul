package org.paingan.zuul.service;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages authentication cases for OAuth2 updating the cookies holding access and refresh tokens accordingly.
 * <p>
 * It can authenticate users, refresh the token cookies should they expire and log users out.
 */
public class OAuth2AuthenticationService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Number of milliseconds to cache refresh token grants so we don't have to repeat them in case of parallel requests.
     */
    private static final long REFRESH_TOKEN_VALIDITY_MILLIS = 10000l;

    /**
     * Used to contact the OAuth2 token endpoint.
     */
    @Autowired
    private final OAuth2TokenEndpointClient authorizationClient;

    /**
     * Helps us with cookie handling.
     */
    @Autowired
    private final OAuth2CookieHelper cookieHelper;

    /**
     * Caches Refresh grant results for a refresh token value so we can reuse them.
     * This avoids hammering UAA in case of several multi-threaded requests arriving in parallel.
     */
//    private final PersistentTokenCache<OAuth2Cookies> recentlyRefreshed;

    


    public OAuth2AuthenticationService(OAuth2TokenEndpointClient authorizationClient, OAuth2CookieHelper cookieHelper) {
        this.authorizationClient = authorizationClient;
        this.cookieHelper = cookieHelper;
//        recentlyRefreshed = new PersistentTokenCache<>(REFRESH_TOKEN_VALIDITY_MILLIS);
    }

	/**
     * Authenticate the user by username and password.
     *
     * @param request  the request coming from the client.
     * @param response the response going back to the server.
     * @param params   the params holding the username, password and rememberMe.
     * @return the OAuth2AccessToken as a ResponseEntity. Will return OK (200), if successful.
     * If the UAA cannot authenticate the user, the status code returned by UAA will be returned.
     */
    public ResponseEntity<OAuth2AccessToken> authenticate(HttpServletRequest request, HttpServletResponse response,
                                                          Map<String, String> params) {
        try {
            String username = params.get("username");
            String password = params.get("password");
            boolean rememberMe = Boolean.valueOf(params.get("rememberMe"));
            OAuth2AccessToken accessToken = authorizationClient.sendPasswordGrant(username, password);
            OAuth2Cookies cookies = new OAuth2Cookies();
            cookieHelper.createCookies(request, accessToken, rememberMe, cookies);
            cookies.addCookiesTo(response);
            if (log.isDebugEnabled()) {
                log.debug("successfully authenticated user {}", params.get("username"));
            }
            return ResponseEntity.ok(accessToken);
        } catch (Exception ex) {
            log.error("failed to get OAuth2 tokens from UAA", ex);
            throw ex;
        }
    }

    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        cookieHelper.clearCookies(httpServletRequest, httpServletResponse);
    }

    
    /**
     * Try to refresh the access token using the refresh token provided as cookie.
     * Note that browsers typically send multiple requests in parallel which means the access token
     * will be expired on multiple threads. We don't want to send multiple requests to UAA though,
     * so we need to cache results for a certain duration and synchronize threads to avoid sending
     * multiple requests in parallel.
     *
     * @param request       the request potentially holding the refresh token.
     * @param response      the response setting the new cookies (if refresh was successful).
     * @param refreshCookie the refresh token cookie. Must not be null.
     * @return the new servlet request containing the updated cookies for relaying downstream.
     */
    public ResponseEntity<OAuth2AccessToken> refreshToken(HttpServletRequest request, HttpServletResponse response, Map<String, String> params) {
   
    	try {
    		String refreshToken = params.get("refreshToken");
	        OAuth2AccessToken accessToken = authorizationClient.sendRefreshGrant(refreshToken);
	        return ResponseEntity.ok(accessToken);
        } catch (Exception ex) {
            log.error("failed to get OAuth2 tokens from UAA", ex);
            throw ex;
        }

    }
}
