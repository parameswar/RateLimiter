package com.app.ratelimiter.filter;

import java.time.LocalDateTime;

public class IncomingRequestsBean {

    private final int maximumNumberOfRequestsAllowed;
    private final LastRequestDetails lastRequestsHolder;

    IncomingRequestsBean(int maximumNumberOfRequestsAllowed) {
        this.maximumNumberOfRequestsAllowed = maximumNumberOfRequestsAllowed;
        this.lastRequestsHolder = new LastRequestDetails(maximumNumberOfRequestsAllowed);
    }

	void addClientRequest() {
        lastRequestsHolder.addRequest();
    }

    public boolean isFull() {
        return lastRequestsHolder.size() >= maximumNumberOfRequestsAllowed;
    }

    public LocalDateTime getOldestRequestTime() {
        return lastRequestsHolder.getOldestRequestTime();
    }

    public int size() {
        return lastRequestsHolder.size();
    }
}