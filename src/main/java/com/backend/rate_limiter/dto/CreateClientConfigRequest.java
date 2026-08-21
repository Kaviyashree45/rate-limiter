package com.backend.rate_limiter.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CreateClientConfigRequest {
        @NotBlank(message="Client Key is required")
        private String clientKey;
        @Positive(message="Capacity must be greater than 0")
        private int capacity;
        @Positive(message="Refill Rate must be greater than 0")
        private double refillRate;
        public CreateClientConfigRequest(){

        }
    public void setClientKey(String clientKey){
        this.clientKey=clientKey;
    }
    public String getClientKey(){
        return clientKey;
    }
    public int getCapacity(){
        return capacity;
    }
    public void setCapacity(int capacity){
        this.capacity=capacity;
    }
    public double getRefillRate(){
        return refillRate;
    }
    public void setRefillRate(double refillRate){
        this.refillRate=refillRate;
    }

}