package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Bean
//    PreAuthFilterFactory preGatewayFilterFactory() {
//        return new PreAuthFilterFactory();
//    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.host("products")
                        .or()
                        .path("/products/**")
                        .filters(f -> {
                                    //f.filter(preGatewayFilterFactory().apply(config -> {}));
                                    return f.rewritePath("^/products", "/");
                                }
                        )
                        .uri("http://localhost:8082")
                )
                .build();
    }

    @EnableWebFluxSecurity
    static class SecurityConfig  {

        @Bean
        SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
            return http.csrf().disable()
                    .authorizeExchange()
                    .pathMatchers("/**").authenticated()
                    .anyExchange().permitAll()
                    .and()
            .addFilterBefore((exchange, chain) -> {
                ServerHttpRequest request = exchange.getRequest();
                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (isValid(authHeader)) return chain.filter(exchange);
                return chain.;
            }, SecurityWebFiltersOrder.AUTHENTICATION)
                    .build();
        }

        private boolean isValid(String authHeader) {
            return "abc".equals(authHeader);
        }

        @Bean
        ReactiveAuthenticationManager reactiveAuthenticationManager(){
            return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsRepository());
        }

        @Bean
        public MapReactiveUserDetailsService userDetailsRepository() {
            User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
            UserDetails admin = userBuilder.username("admin").password("admin12345").roles("USER", "ADMIN").build();
            return new MapReactiveUserDetailsService(admin);
        }
    }

}

//class PreAuthFilterFactory extends AbstractGatewayFilterFactory<PreAuthFilterFactory.Config> {
//
//    public PreAuthFilterFactory() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        // grab configuration from Config object
//        return (exchange, chain) -> {
//            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
//            ServerHttpRequest request = builder.build();
//            return chain.filter(exchange.mutate().request(request).build());
//        };
//    }
//
//    public static class Config {
//        //Put the configuration properties for your filter here
//    }
//
//}
