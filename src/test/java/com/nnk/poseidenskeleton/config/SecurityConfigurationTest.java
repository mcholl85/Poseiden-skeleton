package com.nnk.poseidenskeleton.config;

import com.nnk.springboot.PoseidenSkeletonApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest( classes = PoseidenSkeletonApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigurationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void testAccessDeniedForNonAuthentificatedUser() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUserAccess() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminAccess() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk());
    }
}
