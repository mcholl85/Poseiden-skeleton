package com.nnk.poseidenskeleton.controller;

import com.nnk.springboot.PoseidenSkeletonApplication;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PoseidenSkeletonApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user("user").roles("ADMIN")).with(csrf()))
                .apply(springSecurity())
                .build();
    }

    @AfterAll
    static void clean(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("TRUNCATE TABLE test.users");
    }

    @Test
    @Order(1)
    void testAddUser() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "usernameTest")
                        .param("password", "Password1$")
                        .param("fullname", "fullnameTest")
                        .param("role","ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @Order(2)
    void testAddInvalidUser() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "usernameTest")
                        .param("password", "Password")
                        .param("fullname", "fullnameTest")
                        .param("role","ADMIN"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(model().attribute("user", hasProperty("role", is("ADMIN"))))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(3)
    void testGetAddUser() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @Order(4)
    void testGetUser() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(content().string(containsString("fullnameTest")));
    }

    @Test
    @Order(5)
    void testGetUpdateFormValidId() throws Exception {
        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @Order(7)
    void testUpdateUserValidId() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("username", "usernameTestUpdated")
                        .param("password", "Password$3R")
                        .param("fullname", "fullnameTest")
                        .param("role","ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @Order(8)
    void testUpdateUserInvalidPayload() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("id", "1")
                        .param("username", "usernameTestUpdated")
                        .param("password", "Password$3R")
                        .param("fullname", "")
                        .param("role","ADMIN"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "fullname"))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(9)
    void testDeleteTrade() throws Exception {
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }
}
