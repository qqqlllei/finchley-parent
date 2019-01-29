//package com.server.a.filter;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * Created by lilei on 2017/5/3.
// */
//@Configuration
//public class WeChatWebAppConfigurer implements WebMvcConfigurer {
//
//
//    @Bean
//    public LoginSessionInterceptor loginSessionInterceptor(){
//        return  new LoginSessionInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginSessionInterceptor()).excludePathPatterns("/actuator/**").addPathPatterns("/**");
//    }
//}
