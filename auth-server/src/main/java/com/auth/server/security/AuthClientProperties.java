package com.auth.server.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 李雷 on 2018/7/9.
 */
@ConfigurationProperties(prefix = "oauth.client")
@Component
public class AuthClientProperties {

    private String jwtSigningKey;

    private String authServerClientId;

    private OAuth2ClientProperties[] clients = {};

    private List<Map<String,String>> handlers = new ArrayList<>();

    public OAuth2ClientProperties[] getClients() {
        return clients;
    }

    public void setClients(OAuth2ClientProperties[] clients) {
        this.clients = clients;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }

    public void setJwtSigningKey(String jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }

    public String getAuthServerClientId() {
        return authServerClientId;
    }

    public void setAuthServerClientId(String authServerClientId) {
        this.authServerClientId = authServerClientId;
    }

    public List<Map<String, String>> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<Map<String, String>> handlers) {
        this.handlers = handlers;
    }
}
