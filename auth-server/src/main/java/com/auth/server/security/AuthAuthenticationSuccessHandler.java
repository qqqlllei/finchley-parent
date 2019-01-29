package com.auth.server.security;

import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.AuthSuccessHandler;
import com.auth.server.security.vo.AuthUser;
import com.auth.server.util.ApplicationContextHelper;
import com.auth.server.util.AuthHandlerUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class AuthAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private DefaultTokenServices defaultTokenServices;

	@Autowired
	private AuthClientProperties authClientProperties;



	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

		AuthUser sysUserAuthentication = new AuthUser();
		BeanUtils.copyProperties(authentication.getPrincipal(), sysUserAuthentication);
		String clientId = request.getParameter(SecurityConstant.REQUEST_CLIENT_ID);
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
		TokenRequest tokenRequest = new TokenRequest(null, clientId, clientDetails.getScope(), "custom");
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
		defaultTokenServices.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		OAuth2AccessToken token =  defaultTokenServices.createAccessToken(oAuth2Authentication);
		String authType = request.getParameter(SecurityConstant.AUTH_TYPE_PARM_NAME);
		Map<String,Object> additionalInformation = clientDetails.getAdditionalInformation();
		String authSuccessHandlerBeanName = AuthHandlerUtil.getSuccessHandlerByType(authType,additionalInformation,authClientProperties);
		AuthSuccessHandler authenticationSuccessHandler =  ApplicationContextHelper.getBean(authSuccessHandlerBeanName,AuthSuccessHandler.class);
		authenticationSuccessHandler.onAuthenticationSuccess(request,response,authentication,token,oAuth2Authentication,sysUserAuthentication,clientDetails);
	}

}
