package com.example.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@SpringBootApplication
public class SpringAuthJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthJwtApplication.class, args);
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
    BasicAuthenticationFilter apiKeyAuthorizationFilter() throws Exception {
        return new ApiKeyAuthorizationFilter(authenticationManager());
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthenticationFilter())
                //.addFilter(apiKeyAuthorizationFilter())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return username -> User.withUsername("admin").password("password").build();
    }

    public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


        public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
            this.setAuthenticationManager(authenticationManager);
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            try {
                UserDto dto = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
                return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword(),
                        Collections.emptyList()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
            logger.info(request);
            super.successfulAuthentication(request, response, chain, authResult);
        }
    }

    public class ApiKeyAuthorizationFilter extends BasicAuthenticationFilter {

        public static final String X_API_KEY_HEADER = "X-API-Key";

        protected ApiKeyAuthorizationFilter(AuthenticationManager authenticationManager) {
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

class UserDto {

    private final String username;
    private final String password;

    @JsonCreator
    public UserDto(@JsonProperty("username") String username,
                   @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
