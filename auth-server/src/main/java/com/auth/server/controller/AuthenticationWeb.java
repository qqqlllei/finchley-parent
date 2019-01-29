package com.auth.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.dingding.DingTokenServer;
import com.auth.server.util.WechatUrlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/8 0008.
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationWeb {

    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String WECHAT_APPID="wechatAppId";

    @Autowired
    private DingTokenServer dingTokenServer;

    @RequestMapping(value = { "/checkToken" })
    public Map<String, Object> user(@RequestParam("token") String token) {
        Map<String, Object> result = new HashMap<>();
        try{
            OAuth2AccessToken oAuth2AccessToken = resourceServerTokenServices.readAccessToken(token);

            if (oAuth2AccessToken == null) {
                result.put("resultCode","1111");
                result.put("resultMsg","Token= "+token+" 未识别");
                return result;
            }

            result = oAuth2AccessToken.getAdditionalInformation();

            if(result.containsKey(SecurityConstant.AUTH_SESSION_KEY) && result.containsKey(SecurityConstant.REQUEST_CLIENT_ID)){
                String sessionKey = String.valueOf(result.get(SecurityConstant.AUTH_SESSION_KEY));
                String sessionValue = stringRedisTemplate.opsForValue().get(sessionKey);
                String clientId = String.valueOf(result.get(SecurityConstant.REQUEST_CLIENT_ID));
                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
                stringRedisTemplate.opsForValue().set(sessionKey,sessionValue,clientDetails.getAccessTokenValiditySeconds(), TimeUnit.SECONDS);
                result.put("resultCode","0000");
                return result;
            }


            if (oAuth2AccessToken.isExpired()) {
                result.clear();
                result.put("resultCode","2222");
                result.put("resultMsg","Token= "+token+" 已过期");
                return result;
            }
            result.put("resultCode","0000");

        }catch(Exception e){
            result.put("resultCode","3333");
            result.put("resultMsg","Token= "+token+" 已被损坏");
        }

        return result;
    }




    @RequestMapping(value = "/getWeiXinCodeUrl/{clientId}")
    public String getWeiXinCodeUrl(@PathVariable("clientId") String  clientId){
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        Map<String,Object> additionalInformation =  clientDetails.getAdditionalInformation();
        String oauthUrl = WechatUrlHelper.oauth2buildAuthorizationUrl(String.valueOf(additionalInformation.get(WECHAT_APPID)));
        JSONObject result = new JSONObject();
        String[] urlAtrr = oauthUrl.split("&redirect_uri=");
        String start = "";
        String end = "";
        if(urlAtrr != null && !"".equals(urlAtrr)){
            start = urlAtrr[0];
            end = urlAtrr[1];
        }
        result.put("start",start);
        result.put("end",end);
        return result.toJSONString();
    }


    @RequestMapping(value = "/dingcalBack")
    public String dingcalBack(@RequestParam("code") String  code){
        String phone = dingTokenServer.getUserPhoneByAuthCode(code);
        return phone;
    }
}
