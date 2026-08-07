package com.backend.rate_limiter.dto;

public class CheckRateLimitResponse {
    private boolean allowed;
    public CheckRateLimitResponse(){

    }
    public CheckRateLimitResponse(boolean allowed){
        this.allowed=allowed;
    }
    public void setAllowed(boolean allowed){
        this.allowed=allowed;
    }
    public boolean isAllowed(){
        return allowed;
    }
    
}
