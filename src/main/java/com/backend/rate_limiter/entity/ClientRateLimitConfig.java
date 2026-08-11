package com.backend.rate_limiter.entity;

import jakarta.persistence.*;

@Entity
@Table(name="client_rate_limit_config")
public class ClientRateLimitConfig {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(nullable=false,unique=true)
    private String clientKey;
    @Column(nullable=false)
    private int capacity;
    @Column(nullable=false)
    private double refillRate;
    public ClientRateLimitConfig(){}
    public ClientRateLimitConfig(String clientKey,int capacity, double refillRate){
        this.clientKey=clientKey;
        this.capacity=capacity;
        this.refillRate=refillRate;

    }
    public long getId(){
        return id;
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
        this. capacity=capacity;
    }
    public void setRefillRate(double refillRate){
        this.refillRate=refillRate;
    }
    public double getRefillRate(){
        return refillRate;
    }



    
}
