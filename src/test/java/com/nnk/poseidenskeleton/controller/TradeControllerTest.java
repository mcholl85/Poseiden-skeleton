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
class TradeControllerTest {
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
        jdbcTemplate.execute("TRUNCATE TABLE test.trade");
    }

    @Test
    @Order(1)
    void testAddTrade() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "accountTest")
                        .param("type", "typeTest")
                        .param("buyQuantity", "2.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @Order(2)
    void testAddInvalidTrade() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "")
                        .param("type", "typeTest")
                        .param("buyQuantity", "2.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasFieldErrors("trade", "account"))
                .andExpect(model().attribute("trade", hasProperty("type", is("typeTest"))))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(3)
    void testGetAddTrade() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @Order(4)
    void testGetTrade() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trades"))
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @Order(5)
    void testGetUpdateFormValidId() throws Exception {
        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @Order(6)
    void testGetUpdateFormInvalidId() throws Exception {
        mockMvc.perform(get("/trade/update/10000000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @Order(7)
    void testUpdateTradeValidId() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("account", "accountTest")
                        .param("type", "typeTest")
                        .param("buyQuantity", "2.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @Order(8)
    void testUpdateTradeInvalidPayload() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("account", "")
                        .param("type", "typeTest")
                        .param("buyQuantity", "2.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"))
                .andExpect(model().attributeHasFieldErrors("trade", "account"))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(9)
    void testDeleteTrade() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }
}
