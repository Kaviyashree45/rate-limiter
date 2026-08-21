package com.backend.rate_limiter.repository;

import com.backend.rate_limiter.entity.ClientRateLimitConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRateLimitConfigRepositoryTest {

    @Autowired
    private ClientRateLimitConfigRepository repository;

    @Test
    void shouldFindClientByClientKey() {

        ClientRateLimitConfig config =
                new ClientRateLimitConfig(
                        "repository-test-client",
                        10,
                        1.0
                );

        repository.save(config);

        Optional<ClientRateLimitConfig> result =
                repository.findByClientKey("repository-test-client");

        assertTrue(result.isPresent());

        assertEquals(
                "repository-test-client",
                result.get().getClientKey()
        );

        assertEquals(10, result.get().getCapacity());

        assertEquals(1.0, result.get().getRefillRate());
    }
    @Test
    void shouldReturnEmptyWhenClientDoesNotExist() {

        Optional<ClientRateLimitConfig> result =
                repository.findByClientKey("non-existent-client");

        assertTrue(result.isEmpty());
    }
}