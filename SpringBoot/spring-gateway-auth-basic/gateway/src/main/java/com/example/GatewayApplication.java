package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.web.reactive.function.client.ExchangeStrategies.withDefaults;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @EnableWebFluxSecurity
    static class SecurityConfig  {

        @Bean
        SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
            return http.httpBasic().and()
                    .csrf().disable()
                    .authorizeExchange()
                    .pathMatchers("/**").authenticated()
                    .anyExchange().permitAll()
                    .and()
                    .build();
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
