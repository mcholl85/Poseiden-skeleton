package com.nnk.poseidenskeleton;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BidListControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testAddBidList() throws Exception {
        mockMvc.perform(post("/bidList/validate").contentType(MediaType.APPLICATION_JSON).content("{\"account\": \"accountTest\", \"type\": \"typeTest\", \"bidQuantity\": 2.0}")).andExpect(status().isOk());
    }
}
