package com.auth.server.util;

/**
 * Created by 李雷 on 2018/7/23.
 */
public class WechatUrlHelper {

    public static final String OAUTH2_SCOPE_BASE = "snsapi_base";

    public static String oauth2buildAuthorizationUrl(String appId){
        StringBuilder url = new StringBuilder();
        url.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
        url.append("appid=").append(appId);
        url.append("&redirect_uri=").append("");
        url.append("&response_type=code");
        url.append("&scope=").append(OAUTH2_SCOPE_BASE);
        url.append("#wechat_redirect");
        return url.toString();
    }
}
