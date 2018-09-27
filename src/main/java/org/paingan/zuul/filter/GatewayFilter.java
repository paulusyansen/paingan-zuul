package org.paingan.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class GatewayFilter extends ZuulFilter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    /*
     * Adding authorization header to zuul request header as 
     * zuul omits sensitive headers
     */
    if(request.getHeader("Authorization") != null){
    	ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
    	logger.info("authorization -> {}",request.getHeader("Authorization"));
    }
    
    logger.info("request -> {} request uri -> {}",request, request.getRequestURI());
    return null;
  }

}