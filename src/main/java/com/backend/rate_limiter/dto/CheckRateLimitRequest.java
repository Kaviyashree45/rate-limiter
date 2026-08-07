package com.backend.rate_limiter.dto;


public class CheckRateLimitRequest {
    private String clientKey;
    public  CheckRateLimitRequest(){

    }
    public void setClientKey(String clientKey){
        this.clientKey=clientKey;
    }
    public String getClientKey(){
        return clientKey;
    }
    
}
