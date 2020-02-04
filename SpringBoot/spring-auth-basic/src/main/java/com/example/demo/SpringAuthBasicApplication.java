package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@SpringBootApplication
public class SpringAuthBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthBasicApplication.class, args);
    }

}

@EnableWebSecurity
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests().
                anyRequest().
                authenticated().and().
                httpBasic();
    }
}

@RestController
class UserController {

    @GetMapping
    List<String> users() {
      return asList("Tom", "Jerry");
    }

}
