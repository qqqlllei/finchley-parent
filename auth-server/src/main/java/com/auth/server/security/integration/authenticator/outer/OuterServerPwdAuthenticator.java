package com.auth.server.security.integration.authenticator.outer;

import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.IntegrationAuthentication;
import com.auth.server.security.integration.authenticator.IntegrationAuthenticator;
import com.auth.server.security.integration.authenticator.password.UsernamePasswordAuthenticator;
import com.auth.server.security.vo.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 李雷 on 2018/10/30.
 */
@Component
@Primary
public class OuterServerPwdAuthenticator implements IntegrationAuthenticator {

    @Autowired
    private UsernamePasswordAuthenticator usernamePasswordAuthenticator;

    @Override
    public AuthUser authenticate(IntegrationAuthentication integrationAuthentication) {
        return usernamePasswordAuthenticator.authenticate(integrationAuthentication);
    }

    @Override
    public boolean prepare(IntegrationAuthentication integrationAuthentication, Map<String, Object> additionalInformation, HttpServletRequest request, HttpServletResponse response) {
        return usernamePasswordAuthenticator.prepare(integrationAuthentication,additionalInformation,request,response);
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SecurityConstant.OUTER_SERVER_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication, HttpServletRequest request, HttpServletResponse response) {

    }
}
