//package com.server.a.filter;
//
//import com.gray.core.util.ThreadLocalContext;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.net.URLDecoder;
//
///**
// * Created by 李雷 on 2018/7/13.
// */
//@Component
//public class LoginSessionInterceptor implements HandlerInterceptor {
//
//    private static final String META_DATA_KEY_VERSION = "version";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        String version = request.getHeader(META_DATA_KEY_VERSION);
//        ThreadLocalContext.initHystrixRequestContext(version);
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        ThreadLocalContext.shutdownHystrixRequestContext();
//    }
//}
