package com.csu.jpetstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 * 允许前端项目访问后端 API
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")                    // 允许访问 /api/** 路径
                .allowedOrigins("http://localhost:5173")  // 允许前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")                      // 允许所有请求头
                .allowCredentials(true);                  // 允许携带凭证（Cookie、Token）
    }
}
