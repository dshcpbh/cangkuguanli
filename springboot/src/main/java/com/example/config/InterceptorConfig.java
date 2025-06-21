package com.example.config;

import com.example.interceptors.JWTInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dsh
 */ //  拦截器配置
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptors())
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除登录请求
                .excludePathPatterns("/user/login","/user/register","/files/download/**","/files/upload/**");
    }
}
