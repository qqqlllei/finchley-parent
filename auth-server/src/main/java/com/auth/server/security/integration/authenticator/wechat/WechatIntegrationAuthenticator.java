package com.auth.server.security.integration.authenticator.wechat;

import com.alibaba.fastjson.JSONObject;
import com.auth.server.fegin.LoginAbstractFegin;
import com.auth.server.fegin.WechatFegin;
import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.IntegrationAuthentication;
import com.auth.server.security.integration.authenticator.IntegrationAuthenticator;
import com.auth.server.security.vo.AuthUser;
import com.auth.server.util.ApplicationContextHelper;
import com.auth.server.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 李雷 on 2018/7/10.
 */
@Service
public class WechatIntegrationAuthenticator implements IntegrationAuthenticator {


    private static final String WECHAT_OPENID_PARAM_NAME="openid";

    @Autowired
    private WechatFegin wechatFegin;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public AuthUser authenticate(IntegrationAuthentication integrationAuthentication)  {

        String openId = integrationAuthentication.getUsername();

        String password = integrationAuthentication.getAuthParameterByKey(SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD);

        LoginAbstractFegin loginAbstractFegin = ApplicationContextHelper.getBean(integrationAuthentication.getFindUserClassName(), LoginAbstractFegin.class);
        AuthUser sysUserAuthentication=loginAbstractFegin.findUserById(openId);

        if (sysUserAuthentication != null) {
            sysUserAuthentication.setPassword(passwordEncoder.encode(password));
        }

        return sysUserAuthentication;
    }

    @Override
    public boolean prepare(IntegrationAuthentication integrationAuthentication, Map<String,Object> additionalInformation, HttpServletRequest request, HttpServletResponse response) {
        String password = integrationAuthentication.getAuthParameterByKey(SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD);


        String oAuth2AccessToken = wechatFegin.oauth2getAccessToken(String.valueOf(additionalInformation.get(SecurityConstant.WECHAT_APPID_PARAM_NAME)),
                String.valueOf(additionalInformation.get(SecurityConstant.WECHAT_SECRET_PARAM_NAME)),
                password,
                SecurityConstant.OAUTH2_GET_ACCESS_TOKEN_GRANT_TYPE);
        JSONObject wechatResponse = JSONObject.parseObject(oAuth2AccessToken);
        if(wechatResponse.containsKey(SecurityConstant.WECHAT_REQUEST_ERROR_FLAG)){
            RestResponseUtil.wechatAuthError(response);
            return false;
        }

        String openId =  wechatResponse.getString(WECHAT_OPENID_PARAM_NAME);
        integrationAuthentication.setUsername(openId);

        return true;
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SecurityConstant.WECHAT_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication, HttpServletRequest request, HttpServletResponse response) {

    }
}
