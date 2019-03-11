package com.app.ratelimiter.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "limiterconfig")
public class Config {

    private int limitWindow;
    private int requestsAllowed;
	public int getLimitWindow() {
		return limitWindow;
	}
	public void setLimitWindow(int limitWindow) {
		this.limitWindow = limitWindow;
	}
	public int getRequestsAllowed() {
		return requestsAllowed;
	}
	public void setRequestsAllowed(int requestsAllowed) {
		this.requestsAllowed = requestsAllowed;
	}
   
  
}