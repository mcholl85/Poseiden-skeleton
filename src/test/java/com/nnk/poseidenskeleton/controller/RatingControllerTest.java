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
class RatingControllerTest {
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
        jdbcTemplate.execute("TRUNCATE TABLE test.rating");
    }

    @Test
    @Order(1)
    void testAddRating() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "5")
                        .param("sandpRating", "5")
                        .param("fitchRating", "5")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @Order(2)
    void testAddInvalidRating() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "5")
                        .param("sandpRating", "5")
                        .param("fitchRating", "ERROR")
                        .param("orderNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeHasFieldErrors("rating", "fitchRating"))
                .andExpect(model().attribute("rating", hasProperty("orderNumber", is(1))))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(3)
    void testGetAddRating() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    @Order(4)
    void testGetRating() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ratings"))
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @Order(5)
    void testGetUpdateFormValidId() throws Exception {
        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    @Order(6)
    void testGetUpdateFormInvalidId() throws Exception {
        mockMvc.perform(get("/rating/update/10000000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @Order(7)
    void testUpdateRatingValidId() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "4")
                        .param("sandpRating", "4")
                        .param("fitchRating", "4")
                        .param("orderNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @Order(8)
    void testUpdateRatingInvalidPayload() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "4")
                        .param("sandpRating", "ERROR")
                        .param("fitchRating", "4")
                        .param("orderNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"))
                .andExpect(model().attributeHasFieldErrors("rating", "sandpRating"))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(9)
    void testDeleteRating() throws Exception {
        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }
}
