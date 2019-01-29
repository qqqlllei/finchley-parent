package com.auth.server.security.constants;

/**
 * Created by 李雷 on 2018/8/7.
 */
public class SecurityConstant {

    public static final String AUTH_TYPE_PARM_NAME = "authType";

    public static final String OAUTH_TOKEN_URL = "/oauth/token";

    public static final String COMMON_LOGIN_URL="/authentication/form";

    public static final String AUTH_AUTHORIZED_GRANT_PASSWORD ="password";

    public static final String AUTH_AUTHORIZED_GRANT_USERNAME ="username";

    public static final String AUTH_AUTHORIZED_GRANT_LOGIN_NAME ="loginName";

    public static final String WECHAT_AUTH_TYPE = "wechat";

    public static final String APP_TOKEN_NAME="appToken";

    public static final String WECHAT_LOGIN_CODE_PARAM_NAME="code";

    public static final String SMS_LOGIN_PHONE_PARAM_NAME="phone";

    public static final String WECHAT_CLIENT_ID_PARAM_NAME="clientId";

    public static final String WECHAT_APPID_PARAM_NAME="wechatAppId";

    public static final String WECHAT_SECRET_PARAM_NAME="wechatSecret";

    public static final String WECHAT_TOKEN_PARAM_NAME="wechatToken";

    public static final String WECHAT_AES_KEY_PARAM_NAME="wechatAesKey";

    public static final String OAUTH2_GET_ACCESS_TOKEN_GRANT_TYPE="authorization_code";

    public static final String WECHAT_OPENID_PARAM_NAME="openId";

    public static final String WECHAT_REQUEST_ERROR_FLAG="errcode";

    public static final String OUT_WECHAT_CLIENT_ID_NAME="out-wechat";

    public static final  String SMS_AUTH_TYPE = "sms";


    public static final String DING_DING_LOGIN_CODE_PARAM_NAME="code";


    public static final String DING_DING_AUTH_TYPE = "dingding";

    public static final String OUTER_SERVER_AUTH_TYPE="outerServer";

    public static final String REQUEST_CLIENT_ID = "clientId";

    public static final String TOKEN_VALUE="tokenValue";

    public static final String AUTH_SESSION_KEY = "sessionKey";

    public static final String AUTH_SUCCESS_HANDLER="authSuccessHandler";

    public static final String AUTH_SUCCESS_HANDLER_POSTFIX="AuthSuccessHandler";

    public static final String SMS_AUTH_SUCCESS_HANDLER_SUCCESS="smsAuthSuccessHandler";

    public static final String AUTH_FAILURE_HANDLER="authFailureHandler";

    public static final String AUTH_FIND_USER_INTERFACE_CLASS="findUserClassName";


    public static final String AUTH_DEFAULT_SUCCESS_HANDLER="defaultAuthSuccessHandler";

    public static final String AUTH_DEFAULT_FAILURE_HANDLER="defaultAuthFailureHandler";

    public static final String AUTH_DEFAULT_FIND_USER_INTERFACE_CLASS="com.auth.UserFegin";

    public static final String AUTH_LOGIN_SUCCESS_STATUS="0000";

    public static final String AUTH_LOGIN_FAIL_STATUS="0001";

    public static final String AUTH_LOGIN_CODE_NAME="resultCode";



}
