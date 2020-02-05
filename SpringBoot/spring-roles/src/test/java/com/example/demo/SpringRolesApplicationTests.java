package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class SpringRolesApplicationTests {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    void shouldAllowToAccessProtectedResourceByAdmin() throws Exception {
        mvc.perform(get("/roles").with(httpBasic("Tom", "password")))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(content().json("[\"ADMIN\",\"USER\"]"));
    }

    @Test
    void shouldNotAllowToAccessAdminProtectedResourceByUser() throws Exception {
        mvc.perform(get("/roles").with(httpBasic("Jerry", "password")))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowToAccessProtectedResourceByUser() throws Exception {
        mvc.perform(get("/users").with(httpBasic("Jerry", "password")))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"Tom\",\"Jerry\"]"));
    }

    @Test
    @WithAnonymousUser
    void shouldNotAllowToAuthenticateWithAnonymousUserUsingBasicAuth() throws Exception {
        mvc.perform(get("/")).andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
