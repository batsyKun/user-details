package com.userdetails.userdetails.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files if needed
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}