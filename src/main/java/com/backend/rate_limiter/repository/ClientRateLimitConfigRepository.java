package com.backend.rate_limiter.repository;
import com.backend.rate_limiter.entity.ClientRateLimitConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ClientRateLimitConfigRepository extends JpaRepository<ClientRateLimitConfig,Long> {
  Optional<ClientRateLimitConfig>findByClientKey(String clientKey);
}
