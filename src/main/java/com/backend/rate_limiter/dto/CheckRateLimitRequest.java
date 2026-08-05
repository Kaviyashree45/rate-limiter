package com.backend.rate_limiter.dto;

public class CheckRateLimitRequest {
    private String clientKey;
    public  CheckRateLimitRequest(){

    }
    public void setclientKey(String clientKey){
        this.clientKey=clientKey;
    }
    public String getclientKey(){
        return clientKey;
    }
    
}
