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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PoseidenSkeletonApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BidListControllerTest {
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
        jdbcTemplate.execute("TRUNCATE TABLE test.bidlist");
    }

    @Test
    @Order(1)
    void testAddBidList() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "accountTest")
                        .param("type", "typeTest")
                        .param("bidQuantity", "2.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @Order(2)
    void testAddInvalidBidList() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "accountTest")
                        .param("type", "typeTest")
                        .param("bidQuantity", "ERROR"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasFieldErrors("bidList", "bidQuantity"))
                .andExpect(model().attribute("bidList", hasProperty("account", is("accountTest"))))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(3)
    void testGetAddBidList() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @Order(4)
    void testGetBidList() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(content().string(containsString("accountTest")));
    }

    @Test
    @Order(5)
    void testGetUpdateFormValidId() throws Exception {
        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @Order(6)
    void testGetUpdateFormInvalidId() throws Exception {
        mockMvc.perform(get("/bidList/update/10000000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @Order(7)
    void testUpdateBidValidId() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "UpdatedAccount")
                        .param("type", "updatedType")
                        .param("bidQuantity", "99.9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @Order(8)
    void testUpdateBidInvalidPayload() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "UpdatedAccount")
                        .param("type", "updatedType")
                        .param("bidQuantity", "wrongValue"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().attributeHasFieldErrors("bidList", "bidQuantity"))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(9)
    void testDeleteBidList() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }
}
