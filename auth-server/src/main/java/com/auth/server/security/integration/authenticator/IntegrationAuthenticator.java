package com.auth.server.security.integration.authenticator;


import com.auth.server.security.integration.IntegrationAuthentication;
import com.auth.server.security.vo.AuthUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IntegrationAuthenticator {

    /**
     * 处理集成认证
     * @param integrationAuthentication
     * @return
     */
    AuthUser authenticate(IntegrationAuthentication integrationAuthentication) ;


    /**
     * 进行预处理
     * @param integrationAuthentication
     */
    boolean prepare(IntegrationAuthentication integrationAuthentication, Map<String, Object> additionalInformation, HttpServletRequest request, HttpServletResponse response);

     /**
     * 判断是否支持集成认证类型
     * @param integrationAuthentication
     * @return
     */
    boolean support(IntegrationAuthentication integrationAuthentication);

    /** 认证结束后执行
     * @param integrationAuthentication
     */
    void complete(IntegrationAuthentication integrationAuthentication, HttpServletRequest request, HttpServletResponse response);

}
