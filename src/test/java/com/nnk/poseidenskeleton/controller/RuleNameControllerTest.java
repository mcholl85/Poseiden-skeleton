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
class RuleNameControllerTest {
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
        jdbcTemplate.execute("TRUNCATE TABLE test.rulename");
    }

    @Test
    @Order(1)
    void testAddRuleName() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "nameTest")
                        .param("description", "descriptionTest")
                        .param("json", "jsonTest")
                        .param("template", "templateTest")
                        .param("sqlStr", "sqlTest")
                        .param("sqlPart", "sqlPartTest"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @Order(2)
    void testAddInvalidRuleName() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")
                        .param("description", "descriptionTest")
                        .param("json", "jsonTest")
                        .param("template", "templateTest")
                        .param("sqlStr", "sqlTest")
                        .param("sqlPart", "sqlPartTest"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "name"))
                .andExpect(model().attribute("ruleName", hasProperty("description", is("descriptionTest"))))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(3)
    void testGetAddRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @Order(4)
    void testGetRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @Order(5)
    void testGetUpdateFormValidId() throws Exception {
        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @Order(6)
    void testGetUpdateFormInvalidId() throws Exception {
        mockMvc.perform(get("/ruleName/update/10000000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @Order(7)
    void testUpdateRuleNameValidId() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("name", "nameTestUpdated")
                        .param("description", "descriptionTest")
                        .param("json", "jsonTest")
                        .param("template", "templateTest")
                        .param("sqlStr", "sqlTest")
                        .param("sqlPart", "sqlPartTest"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @Order(8)
    void testUpdateRuleNameInvalidPayload() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("name", "")
                        .param("description", "descriptionTest")
                        .param("json", "jsonTest")
                        .param("template", "templateTest")
                        .param("sqlStr", "sqlTest")
                        .param("sqlPart", "sqlPartTest"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "name"))
                .andExpect(model().errorCount(1));
    }

    @Test
    @Order(9)
    void testDeleteRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }
}
