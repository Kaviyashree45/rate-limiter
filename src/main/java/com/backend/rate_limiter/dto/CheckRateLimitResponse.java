package com.backend.rate_limiter.dto;

public class CheckRateLimitResponse {
    private boolean allowed;
    private int remaining;
    private int limit;
    public CheckRateLimitResponse(){

    }
    public CheckRateLimitResponse(boolean allowed,int remaining,int limit){
        this.allowed=allowed;
        this.remaining=remaining;
        this.limit=limit;
    }
    public void setAllowed(boolean allowed){
        this.allowed=allowed;
    }
    public boolean isAllowed(){
        return allowed;
    }
    public int getRemaining(){
        return remaining;
    }
    public void setRemaining(int remaining){
        this.remaining=remaining;
    }
    public int getLimit(){
        return limit;
    }
    public void setLimit(int limit){
        this.limit=limit;
    }

}
