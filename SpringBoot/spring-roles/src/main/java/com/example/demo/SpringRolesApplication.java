package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@SpringBootApplication
public class SpringRolesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRolesApplication.class, args);
    }

}


@EnableWebSecurity
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests().
                antMatchers("/roles").hasRole(UserController.Role.ADMIN.name()).
                antMatchers("/users").hasAnyRole(
                        UserController.Role.ADMIN.name(),
                        UserController.Role.USER.name()).
                anyRequest().
                authenticated().and().
                httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("Tom")
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .roles(UserController.Role.ADMIN.name(), UserController.Role.USER.name()).build();
        UserDetails user = User.withUsername("Jerry")
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .roles(UserController.Role.USER.name()).build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}

@RestController
class UserController {

    @GetMapping("/users")
    List<String> users() {
        return asList("Tom", "Jerry");
    }

    @GetMapping("/roles")
    List<String> roles() {
        return Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toList());
    }

    enum Role {
        ADMIN, USER
    }
}
