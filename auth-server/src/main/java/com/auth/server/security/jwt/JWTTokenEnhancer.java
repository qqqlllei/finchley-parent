package com.auth.server.security.jwt;

import com.auth.server.security.vo.AuthUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class JWTTokenEnhancer implements TokenEnhancer {

    private static String OPEN_ID="openId";

    private static String CLIENT_ID="clientId";



    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String clientId = authentication.getOAuth2Request().getClientId();
        Map<String, Object> additionalInfo = new HashMap<>();
        AuthUser user = (AuthUser) authentication.getPrincipal();
        additionalInfo.put(OPEN_ID, user.getId());
        additionalInfo.put(CLIENT_ID,clientId);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
