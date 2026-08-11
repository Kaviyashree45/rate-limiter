package com.backend.rate_limiter.dto;

public class CreateClientConfigRequest {
        private String clientKey;
        private int capacity;
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