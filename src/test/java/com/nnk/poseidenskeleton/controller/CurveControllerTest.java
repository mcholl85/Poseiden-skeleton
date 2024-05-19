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
class CurveControllerTest {
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
        jdbcTemplate.execute("TRUNCATE TABLE test.curvepoint");
    }

    @Test
    @Order(1)
    void testAddCurvePoint() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "1")
                        .param("term", "2.0")
                        .param("value", "2.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @Order(2)
    void testAddInvalidCurvePoint() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "1")
                        .param("term", "ERROR")
                        .param("value", "2.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "term"))
                .andExpect(model().attribute("curvePoint", hasProperty("curveId", is(1))))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(3)
    void testGetAddCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @Order(4)
    void testGetCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(content().string(containsString("2.0")));
    }

    @Test
    @Order(5)
    void testGetUpdateFormValidId() throws Exception {
        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @Order(6)
    void testGetUpdateFormInvalidId() throws Exception {
        mockMvc.perform(get("/curvePoint/update/10000000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @Order(7)
    void testUpdateCurvePointValidId() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "1")
                        .param("term", "99.9")
                        .param("value", "99.9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @Order(8)
    void testUpdateCurvePointInvalidPayload() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId", "1")
                        .param("term", "99.9")
                        .param("value", "wrongValue"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "value"))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(9)
    void testDeleteCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }
}
