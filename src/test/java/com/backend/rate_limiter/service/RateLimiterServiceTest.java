package com.backend.rate_limiter.service;

import com.backend.rate_limiter.entity.ClientRateLimitConfig;
import com.backend.rate_limiter.exception.ClientNotFoundException;
import com.backend.rate_limiter.exception.RateLimitExceededException;
import com.backend.rate_limiter.repository.ClientRateLimitConfigRepository;
import com.backend.rate_limiter.strategy.TokenBucket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimiterServiceTest {

    private ClientRateLimitConfigRepository repository;
    private RateLimiterService service;

    @BeforeEach
    void setUp() {
        repository = mock(ClientRateLimitConfigRepository.class);
        service = new RateLimiterService(repository);
    }

    @Test
    void shouldAllowRequestForValidClient() {

        ClientRateLimitConfig config =
                new ClientRateLimitConfig(
                        "client-test",
                        10,
                        1.0
                );

        when(repository.findByClientKey("client-test"))
                .thenReturn(Optional.of(config));

        TokenBucket.RateLimitResult result =
                service.checkRequest("client-test");

        assertTrue(result.isAllowed());
        assertEquals(10, result.getLimit());
        assertEquals(9, result.getRemaining());

        verify(repository).findByClientKey("client-test");
    }

    @Test
    void shouldThrowClientNotFoundException() {

        when(repository.findByClientKey("unknown-client"))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> service.checkRequest("unknown-client")
        );

        verify(repository).findByClientKey("unknown-client");
    }

    @Test
    void shouldReuseSameBucketForSameClient() {

        ClientRateLimitConfig config =
                new ClientRateLimitConfig(
                        "client-test",
                        2,
                        1.0
                );

        when(repository.findByClientKey("client-test"))
                .thenReturn(Optional.of(config));

        TokenBucket.RateLimitResult first =
                service.checkRequest("client-test");

        TokenBucket.RateLimitResult second =
                service.checkRequest("client-test");

        assertTrue(first.isAllowed());
        assertTrue(second.isAllowed());

        assertEquals(1, first.getRemaining());
        assertEquals(0, second.getRemaining());

        verify(repository, times(1))
                .findByClientKey("client-test");
    }

    @Test
    void shouldThrowRateLimitExceededExceptionWhenTokensAreExhausted() {

        ClientRateLimitConfig config =
                new ClientRateLimitConfig(
                        "client-test",
                        2,
                        0
                );

        when(repository.findByClientKey("client-test"))
                .thenReturn(Optional.of(config));

        service.checkRequest("client-test");
        service.checkRequest("client-test");

        RateLimitExceededException exception =
                assertThrows(
                        RateLimitExceededException.class,
                        () -> service.checkRequest("client-test")
                );

        assertTrue(
                exception.getMessage()
                        .contains("Rate limit exceeded")
        );
    }
}