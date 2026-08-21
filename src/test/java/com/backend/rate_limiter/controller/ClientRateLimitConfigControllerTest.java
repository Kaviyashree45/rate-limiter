package com.backend.rate_limiter.controller;

import com.backend.rate_limiter.dto.CreateClientConfigRequest;
import com.backend.rate_limiter.entity.ClientRateLimitConfig;
import com.backend.rate_limiter.service.ClientRateLimitConfigService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientRateLimitConfigController.class)
class ClientRateLimitConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientRateLimitConfigService service;

    @Test
    void shouldRejectEmptyClientKey() throws Exception {

        mockMvc.perform(
                post("/api/v1/admin/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientKey": "",
                                    "capacity": 10,
                                    "refillRate": 1
                                }
                                """)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectInvalidCapacity() throws Exception {

        mockMvc.perform(
                post("/api/v1/admin/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientKey": "client-invalid",
                                    "capacity": -1,
                                    "refillRate": 1
                                }
                                """)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectInvalidRefillRate() throws Exception {

        mockMvc.perform(
                post("/api/v1/admin/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientKey": "client-invalid",
                                    "capacity": 10,
                                    "refillRate": -1
                                }
                                """)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateClientConfig() throws Exception {

        ClientRateLimitConfig config =
                new ClientRateLimitConfig(
                        "client-test",
                        10,
                        1.0
                );

        when(service.createConfig(any(CreateClientConfigRequest.class)))
                .thenReturn(config);

        mockMvc.perform(
                post("/api/v1/admin/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientKey": "client-test",
                                    "capacity": 10,
                                    "refillRate": 1.0
                                }
                                """)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clientKey").value("client-test"))
        .andExpect(jsonPath("$.capacity").value(10))
        .andExpect(jsonPath("$.refillRate").value(1.0));
    }
}