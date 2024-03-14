package bytebrewers.bitpod.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bytebrewers.bitpod.util.Helper;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional // we can use this to ensure it doesn't really insert it on database
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    @Order(1)
    @Test
    void testLoginAsSuperAdminSuccess() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
        assertNotNull(token);
    }

    @Order(2)
    @Test
    void testRegisterAdminSuccess() throws Exception {
        ResultActions result = Helper.registerAdmin(mockMvc, objectMapper);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals("admin2@gmail.com", data.get("email"));
        });
    }

    @Order(3)
    @Test
    void testRegisterMemberSucces() throws Exception {
        ResultActions result = Helper.registerMember(mockMvc, objectMapper);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals("member@email.com", data.get("email"));
        });
    }
}
