package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@SpringBootApplication
public class SpringAuthApiKeyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthApiKeyApplication.class, args);
    }

}

@EnableWebSecurity
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String SECURITY_TOKEN;

    public SecurityConfig(@Value("${security_token}") String securityToken) {
        this.SECURITY_TOKEN = securityToken;
    }

    @Bean
    ApiKeyFilter apiKeyFilter() throws Exception {
        return new ApiKeyFilter(authenticationManager());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().addFilter(apiKeyFilter())
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public class ApiKeyFilter extends BasicAuthenticationFilter {

        public static final String X_API_KEY_HEADER = "X-API-Key";

        protected ApiKeyFilter(AuthenticationManager authenticationManager) {
            super(authenticationManager);
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            final String rawToken = apiKeyHeader(request);
            Authentication token = null;
            if (isValid(rawToken)) {
                token = new UsernamePasswordAuthenticationToken(rawToken, rawToken, new ArrayList<>());
            }
            SecurityContextHolder.getContext().setAuthentication(token);
            chain.doFilter(request, response);
        }

        private String apiKeyHeader(HttpServletRequest request) {
            return request.getHeader(X_API_KEY_HEADER);
        }

        private boolean isValid(String token) {
            return SECURITY_TOKEN.equals(token);
        }

    }
}

@RestController
class UserController {

    @GetMapping
    List<String> users() {
      return asList("Tom", "Jerry");
    }

}
