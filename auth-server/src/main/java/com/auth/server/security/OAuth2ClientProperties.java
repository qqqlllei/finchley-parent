package com.auth.server.security;

/**
 * Created by 李雷 on 2018/7/9.
 */
public class OAuth2ClientProperties {

    private String clientId;
    private String clientSecret;
    private int accessTokenValidateSeconds = 2592000;
    private int refreshTokenValiditySeconds = 2592000;
    private String scope;
    private String clientType;
    private String wechatAppId;
    private String wechatSecret;
    private String wechatToken;
    private String wechatAesKey;
    private String findUserClassName;
    private String smsAuthSuccessHandler;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenValidateSeconds() {
        return accessTokenValidateSeconds;
    }

    public void setAccessTokenValidateSeconds(int accessTokenValidateSeconds) {
        this.accessTokenValidateSeconds = accessTokenValidateSeconds;
    }

    public int getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getWechatAppId() {
        return wechatAppId;
    }

    public void setWechatAppId(String wechatAppId) {
        this.wechatAppId = wechatAppId;
    }

    public String getWechatSecret() {
        return wechatSecret;
    }

    public void setWechatSecret(String wechatSecret) {
        this.wechatSecret = wechatSecret;
    }

    public String getWechatToken() {
        return wechatToken;
    }

    public void setWechatToken(String wechatToken) {
        this.wechatToken = wechatToken;
    }

    public String getWechatAesKey() {
        return wechatAesKey;
    }

    public void setWechatAesKey(String wechatAesKey) {
        this.wechatAesKey = wechatAesKey;
    }

    public String getFindUserClassName() {
        return findUserClassName;
    }

    public void setFindUserClassName(String findUserClassName) {
        this.findUserClassName = findUserClassName;
    }

    public String getSmsAuthSuccessHandler() {
        return smsAuthSuccessHandler;
    }

    public void setSmsAuthSuccessHandler(String smsAuthSuccessHandler) {
        this.smsAuthSuccessHandler = smsAuthSuccessHandler;
    }
}
