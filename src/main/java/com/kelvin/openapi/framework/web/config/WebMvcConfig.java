package com.kelvin.openapi.framework.web.config;

import com.kelvin.openapi.framework.web.core.interceptor.GlobalApiLimiterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: 范显
 * @Date: 2024/3/19 11:28
 **/
//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalApiLimiterInterceptor());
    }
}
