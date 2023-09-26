//package com.example.demo;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**") // API 경로 패턴
//                .allowedOrigins("http://localhost:3000") // 허용된 출처
//                .allowedMethods("GET", "POST", "PUT", "DELETE"); // 허용된 HTTP 메서드
//    }
//}
