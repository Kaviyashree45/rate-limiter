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
    public synchronized RateLimitResult allowRequest(){
           refillTokens();
            if(availableTokens>0){
                availableTokens--;
                return new RateLimitResult(
                    true,
                    capacity,
                    availableTokens
                );
            }
            return new RateLimitResult(
                false, 
                capacity, 
                availableTokens
            );
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
    public synchronized long getRetryAfterSeconds(){
        refillTokens();
        if(availableTokens>0){
            return 0;
        }
        if(refillRate<=0){
            return -1;
        }
        return (long)Math.ceil(1.0/refillRate);
        

    }    

public static class RateLimitResult{
    private final boolean allowed;
    private final int remaining;
    private final int limit;
    public RateLimitResult(boolean allowed,int limit,int remaining) {
        this.allowed = allowed;
        this.limit = limit;
        this.remaining = remaining;
    }
    public boolean isAllowed(){
        return allowed;
    }
    public int getRemaining(){
        return remaining;
    }
    public int getLimit(){
        return limit;
    }
}
}
