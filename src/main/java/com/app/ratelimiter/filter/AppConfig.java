package com.app.ratelimiter.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.app.ratelimiter"})
public class AppConfig implements WebMvcConfigurer {
    public AppConfig() { }

 
    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitingFilterRegistrationBean(RateLimitFilter filter) {
        final FilterRegistrationBean<RateLimitFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(filter);
        filterRegBean.addUrlPatterns("/*");
        return filterRegBean;
    }
}