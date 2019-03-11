package com.app.ratelimiter.filter;

import javax.annotation.PreDestroy;
import javax.cache.Cache;

import org.cache2k.Cache2kBuilder;
import org.cache2k.jcache.ExtendedMutableConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Caching;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

	private final int rateLimitWindowInSeconds;
	private final Cache<String, IncomingRequestsBean> requestsCache;
	private final int maximumNumberOfRequestsAllowed;

	@Autowired
	public RateLimitService(Config rateLimitingConfig) {
		rateLimitWindowInSeconds = rateLimitingConfig.getLimitWindow();
		maximumNumberOfRequestsAllowed = rateLimitingConfig.getRequestsAllowed();
		requestsCache = Caching.getCachingProvider().getCacheManager().createCache("requestsCache",
				ExtendedMutableConfiguration.of(Cache2kBuilder.of(String.class, IncomingRequestsBean.class)
						.entryCapacity(1000).expireAfterWrite(rateLimitWindowInSeconds + 20, TimeUnit.SECONDS)));
	}

	boolean isRequestAllowed(String clientIP) {
		return isRequestWithinLimitWindow(clientIP);
	}

	private boolean isRequestWithinLimitWindow(String clientIp) {
		IncomingRequestsBean clientRequest = retrieveClientRequestFromCache(clientIp);
		if (clientRequest.isFull()) {
			LocalDateTime startOfWindow = LocalDateTime.now().minusSeconds(rateLimitWindowInSeconds);
			boolean oldestRequestIsWithinWindow = clientRequest.getOldestRequestTime().isAfter(startOfWindow);
			return !oldestRequestIsWithinWindow;
		} else {
			return true;
		}
	}

	void updateRequest(String clientIp) {
		IncomingRequestsBean clientRequests = retrieveClientRequestFromCache(clientIp);
		clientRequests.addClientRequest();
		requestsCache.put(clientIp, clientRequests);
	}

	private IncomingRequestsBean retrieveClientRequestFromCache(String clientIp) {
		IncomingRequestsBean clientRequests = requestsCache.get(clientIp);
		if (clientRequests == null) {
			clientRequests = new IncomingRequestsBean(maximumNumberOfRequestsAllowed);
		}
		return clientRequests;
	}

	@PreDestroy
	public void onPreDestroy() {
		requestsCache.close();
	}
}