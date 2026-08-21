package com.backend.rate_limiter.controller;
import com.backend.rate_limiter.exception.ClientNotFoundException;
import com.backend.rate_limiter.dto.CheckRateLimitRequest;
import com.backend.rate_limiter.exception.ClientNotFoundException;
import com.backend.rate_limiter.exception.RateLimitExceededException;
import com.backend.rate_limiter.service.RateLimiterService;
import com.backend.rate_limiter.strategy.TokenBucket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(RateLimiterController.class)
class RateLimiterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RateLimiterService rateLimiterService;

@Test
void shouldAllowRequest() throws Exception {

    TokenBucket.RateLimitResult result =
            new TokenBucket.RateLimitResult(
                    true,
                    10,
                    9
            );

    when(rateLimiterService.checkRequest(anyString()))
            .thenReturn(result);

        var results = mockMvc.perform(
                post("/api/v1/rate-limit/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "clientKey": "client-test"
                                }
                                """)
        ).andReturn();

        System.out.println("STATUS = "
                + results.getResponse().getStatus());

        System.out.println("BODY = "
                + results.getResponse().getContentAsString());

        System.out.println("LIMIT = "
                + results.getResponse().getHeader("X-RateLimit-Limit"));

        System.out.println("REMAINING = "
                + results.getResponse().getHeader("X-RateLimit-Remaining"));
}
    @Test
    void shouldReturn429WhenRateLimitExceeded() throws Exception {

        when(rateLimiterService.checkRequest(anyString()))
                .thenThrow(
                    new RateLimitExceededException(
                        "Rate limit exceeded for the client Key:client-test",
                        5
                    )
                );

        mockMvc.perform(
                post("/api/v1/rate-limit/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientKey": "client-test"
                                }
                                """)
            )
            .andExpect(status().isTooManyRequests())
            .andExpect(header().string("Retry-After", "5"))
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message")
                    .value("Rate limit exceeded for the client Key:client-test"));
    }
    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {

        when(rateLimiterService.checkRequest(anyString()))
                .thenThrow(
                    new ClientNotFoundException(
                        "Client not found"
                    )
                );

        mockMvc.perform(
                post("/api/v1/rate-limit/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientKey": "client-does-not-exist"
                                }
                                """)
        )
        .andDo(print())
        .andExpect(status().isNotFound());
    }
}