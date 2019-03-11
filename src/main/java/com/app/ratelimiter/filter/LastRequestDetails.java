package com.app.ratelimiter.filter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LastRequestDetails {

    private final List<LocalDateTime> recentRequestsTimeStamps;
    private final int capacity;

    LastRequestDetails(int capacity) {
        this.capacity = capacity;
        recentRequestsTimeStamps = Collections.synchronizedList(new LinkedList<>());
    }

    void addRequest() {
        if (recentRequestsTimeStamps.size() >= capacity) {
            recentRequestsTimeStamps.remove(0);
        }
        recentRequestsTimeStamps.add(LocalDateTime.now());
    }

    LocalDateTime getOldestRequestTime() {
        return recentRequestsTimeStamps.get(0);
    }

    public int size() {
        return recentRequestsTimeStamps.size();
    }
}