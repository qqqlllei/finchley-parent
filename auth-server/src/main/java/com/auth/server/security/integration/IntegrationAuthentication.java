package com.auth.server.security.integration;


import java.util.Map;


public class IntegrationAuthentication {

    private String authType;
    private String username;
    private String findUserClassName;
    private Map<String,String> authParameters;

    public String getAuthParameterByKey(String paramter){
        String values = this.authParameters.get(paramter);
        return values;
    }


    public Map<String, String> getAuthParameters() {
        return authParameters;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthParameters(Map<String, String> authParameters) {
        this.authParameters = authParameters;
    }

    public String getFindUserClassName() {
        return findUserClassName;
    }

    public void setFindUserClassName(String findUserClassName) {
        this.findUserClassName = findUserClassName;
    }
}
