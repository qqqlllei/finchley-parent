package com.auth.server.security;

import com.auth.server.security.constants.SecurityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component("propertiesClientDetailsService")
public class PropertiesClientDetailsServiceImpl implements ClientDetailsService {

	private Logger logger = LoggerFactory.getLogger(PropertiesClientDetailsServiceImpl.class);

	private ClientDetailsService clientDetailsService;



	@Autowired
	private AuthClientProperties authClientProperties;

	/**
	 * Init.
	 */
	@PostConstruct
	public void init() {
		InMemoryClientDetailsServiceBuilder builder = new InMemoryClientDetailsServiceBuilder();
		if (authClientProperties!=null && authClientProperties.getClients()!=null && authClientProperties.getClients().length>0) {
			for (OAuth2ClientProperties client : authClientProperties.getClients()) {
				Map<String,String> additionalInformation = new HashMap<>();
				if(SecurityConstant.WECHAT_AUTH_TYPE.equals(client.getClientType())){
					additionalInformation.put(SecurityConstant.WECHAT_APPID_PARAM_NAME,client.getWechatAppId());
					additionalInformation.put(SecurityConstant.WECHAT_SECRET_PARAM_NAME,client.getWechatSecret());
					additionalInformation.put(SecurityConstant.WECHAT_TOKEN_PARAM_NAME,client.getWechatToken());
					additionalInformation.put(SecurityConstant.WECHAT_AES_KEY_PARAM_NAME,client.getWechatAesKey());
				}

				//add default handler
				additionalInformation.put(SecurityConstant.AUTH_DEFAULT_SUCCESS_HANDLER,SecurityConstant.AUTH_DEFAULT_SUCCESS_HANDLER);
				additionalInformation.put(SecurityConstant.AUTH_DEFAULT_FAILURE_HANDLER,SecurityConstant.AUTH_DEFAULT_FAILURE_HANDLER);
				additionalInformation.put(SecurityConstant.AUTH_FIND_USER_INTERFACE_CLASS,SecurityConstant.AUTH_DEFAULT_FIND_USER_INTERFACE_CLASS);

				//add custom findUserClassName
				if(StringUtils.isNotBlank(client.getFindUserClassName())){
					additionalInformation.put(SecurityConstant.AUTH_FIND_USER_INTERFACE_CLASS,client.getFindUserClassName());
				}

				// add sms auth success handler
				if(StringUtils.isNotBlank(client.getSmsAuthSuccessHandler())){
					additionalInformation.put(SecurityConstant.SMS_AUTH_SUCCESS_HANDLER_SUCCESS,client.getSmsAuthSuccessHandler());
				}



				builder.withClient(client.getClientId())
						.secret(client.getClientSecret())
						.authorizedGrantTypes(SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD)
						.accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
						.refreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds())
						.scopes(client.getScope()).additionalInformation(additionalInformation);

			}
		}
		try {
			clientDetailsService = builder.build();
		} catch (Exception e) {
			logger.error("init={}", e.getMessage(), e);
		}
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		return clientDetailsService.loadClientByClientId(clientId);
	}
}
