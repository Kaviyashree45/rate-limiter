package com.backend.rate_limiter.service;
import org.springframework.stereotype.Service;
import com.backend.rate_limiter.repository.ClientRateLimitConfigRepository;
import com.backend.rate_limiter.entity.ClientRateLimitConfig;
import com.backend.rate_limiter.dto.CreateClientConfigRequest;
@Service
public class ClientRateLimitConfigService {
    private final ClientRateLimitConfigRepository repository;
    public ClientRateLimitConfigService(ClientRateLimitConfigRepository repository) {
        this.repository = repository;
    }
    public ClientRateLimitConfig createConfig(CreateClientConfigRequest request){
        ClientRateLimitConfig config = new ClientRateLimitConfig(request.getClientKey(),request.getCapacity(),request.getRefillRate());
        return repository.save(config);
    }
  
}
