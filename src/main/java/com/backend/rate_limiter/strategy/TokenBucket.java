package com.backend.rate_limiter.strategy;
import java.time.Instant;
import java.time.Duration;

public class TokenBucket {
    private final int capacity;
    private int availableTokens;
    private final double refillRate;
    private Instant lastRefillTime;

    public TokenBucket(int capacity,double refillRate){
        this.capacity=capacity;
        this.availableTokens=capacity;
        this.refillRate=refillRate;
        this.lastRefillTime=Instant.now();
    }
    public int getCapacity(){
        return capacity;
    }
    public int getAvailableTokens(){
        return availableTokens;
    }
    public  void setAvailableTokens(int avalaibleTokens){
        this.availableTokens=availableTokens;
    }
    public double getRefillRate(){
        return refillRate;
    }
    public Instant getLastRefillTime(){
        return lastRefillTime;
    }
    public void setLastRefillTime(Instant LastRefillTime){
        this.lastRefillTime=LastRefillTime;
    }
    public synchronized boolean allowRequest(){
           refillTokens();
            if(availableTokens>0){
                availableTokens--;
                return true;
            }
            return false;
    }
    public void refillTokens(){
        Instant now=Instant.now();
        long elapsedSeconds=Duration.between(lastRefillTime,now).getSeconds();
        if(elapsedSeconds<=0){
            return;
        }
        int toAddTokens=(int) (elapsedSeconds*refillRate);
        availableTokens=Math.min(capacity,availableTokens+toAddTokens);
        lastRefillTime=now;
    }
    
}
