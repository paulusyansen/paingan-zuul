package org.paingan.zuul.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;

@Component
@Primary
@EnableAutoConfiguration
public class SwaggerResourcesConfiguration implements SwaggerResourcesProvider {
 
	private final RouteLocator routeLocator;
	
	
	public SwaggerResourcesConfiguration(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }
	
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
//        resources.add(swaggerResource("paingan-zuul", "/v2/api-docs", "2.0"));
//        resources.add(swaggerResource("paingan-member", "/member/v2/api-docs", "2.0"));
//        resources.add(swaggerResource("paingan-order", "/order/v2/api-docs", "2.0"));
//        resources.add(swaggerResource("paingan-oauth2", "/uaa/v2/api-docs", "2.0"));
        
        //Add the default swagger resource that correspond to the gateway's own swagger doc
        resources.add(swaggerResource("default", "/v2/api-docs", "2.0"));

        //Add the registered microservices swagger docs as additional swagger resources
        List<Route> routes = routeLocator.getRoutes();
        routes.forEach(route -> {
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "2.0"));
        });
        
        return resources;
    }
 
    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
 
}