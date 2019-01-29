package com.auth.server.security.integration.authenticator.sms;

import com.auth.server.fegin.LoginAbstractFegin;
import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.IntegrationAuthentication;
import com.auth.server.security.integration.authenticator.IntegrationAuthenticator;
import com.auth.server.security.vo.AuthUser;
import com.auth.server.util.ApplicationContextHelper;
import com.auth.server.util.RestResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class SmsIntegrationAuthenticator implements IntegrationAuthenticator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;





    @Override
    public AuthUser authenticate(IntegrationAuthentication integrationAuthentication) {

        String code = integrationAuthentication.getAuthParameterByKey(SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD);


        String phone = integrationAuthentication.getAuthParameterByKey(SecurityConstant.SMS_LOGIN_PHONE_PARAM_NAME);

        //get user by phone
        LoginAbstractFegin loginAbstractFegin = ApplicationContextHelper.getBean(integrationAuthentication.getFindUserClassName(), LoginAbstractFegin.class);
        AuthUser sysUserAuthentication = loginAbstractFegin.findUserByPhone(phone);
        if (sysUserAuthentication != null) {
            sysUserAuthentication.setPassword(passwordEncoder.encode(code));
        }


        return sysUserAuthentication;
    }

    @Override
    public boolean prepare(IntegrationAuthentication integrationAuthentication, Map<String,Object> additionalInformation, HttpServletRequest request, HttpServletResponse response) {
        String smsCode = integrationAuthentication.getAuthParameterByKey(SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD);
        String username = integrationAuthentication.getAuthParameterByKey(SecurityConstant.SMS_LOGIN_PHONE_PARAM_NAME);

        String code = stringRedisTemplate.opsForValue().get(username+"_"+smsCode);
        if (StringUtils.isBlank(code)) {
            RestResponseUtil.smsCodeError(response);
            return false;
        }
        return true;
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SecurityConstant.SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication,HttpServletRequest request, HttpServletResponse response) {

    }
}
