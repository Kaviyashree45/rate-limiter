package com.backend.rate_limiter.dto;

public class CheckRateLimitResponse {
    private boolean allowed;
    public CheckRateLimitResponse(){

    }
    public CheckRateLimitResponse(boolean allowed){
        this.allowed=allowed;
    }
    public void setIsAllowed(boolean allowed){
        this.allowed=allowed;
    }
    public boolean isAllowed(){
        return allowed;
    }
    
}
