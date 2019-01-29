package com.auth.server.security.integration.authenticator.dingding;

import com.auth.server.fegin.UserFegin;
import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.dingding.DingTokenServer;
import com.auth.server.security.integration.IntegrationAuthentication;
import com.auth.server.security.integration.authenticator.IntegrationAuthenticator;
import com.auth.server.security.vo.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 李雷 on 2018/7/24.
 */
@Service
public class DingDingIntegrationAuthenticator implements IntegrationAuthenticator {



    @Autowired
    private DingTokenServer dingTokenServer;

    @Autowired
    private UserFegin userFegin;

    @Override
    public AuthUser authenticate(IntegrationAuthentication integrationAuthentication) {
        String phone = integrationAuthentication.getUsername();
        AuthUser sysUserAuthentication = userFegin.findUserByPhoneNumber(phone);
        return sysUserAuthentication;
    }

    @Override
    public boolean prepare(IntegrationAuthentication integrationAuthentication, Map<String,Object> additionalInformation , HttpServletRequest request, HttpServletResponse response) {
        String code = integrationAuthentication.getAuthParameterByKey(SecurityConstant.DING_DING_LOGIN_CODE_PARAM_NAME);
        String phone = dingTokenServer.getUserPhoneByAuthCode(code);
        integrationAuthentication.setUsername(phone);

        return true;
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SecurityConstant.DING_DING_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication, HttpServletRequest request, HttpServletResponse response) {

    }
}
