package com.auth.server.security.dingding;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by 李雷 on 2018/7/20.
 */
@Service
public class DingTokenServer {

    public static final String GET_TOKEN_SUCCESS="ok";

    public static final int TOKEN_TIME_OUT=110;

    public static final String DING_DING_SNS_TOKEN_REDIS_NAME="ding_ding_sns_token_";

    public static final String DING_DING_COM_TOKEN_REDIS_NAME="ding_ding_com_token_";

    public static final String DING_DING_ERROR_MESSAGE_NAME="errmsg";

    public static final String DING_DING_ACCESS_TOKEN_NAME="access_token";


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DingFegin dingFegin;

    @Autowired
    private DingDingProperties dingDingProperties;

    public String getSnsToken(){
        String accessToken = stringRedisTemplate.opsForValue().get(DING_DING_SNS_TOKEN_REDIS_NAME+dingDingProperties.getAppid());
        if(accessToken ==null){
            JSONObject result = dingFegin.getSnsToken(dingDingProperties.getAppid(),dingDingProperties.getAppsecret());
            if(result.containsKey(DING_DING_ERROR_MESSAGE_NAME) && GET_TOKEN_SUCCESS.equals(result.getString(DING_DING_ERROR_MESSAGE_NAME))){
                accessToken= result.getString(DING_DING_ACCESS_TOKEN_NAME);
                stringRedisTemplate.opsForValue().set(DING_DING_SNS_TOKEN_REDIS_NAME+dingDingProperties.getAppid(),accessToken,TOKEN_TIME_OUT, TimeUnit.MINUTES);
            }
        }

        return accessToken;
    }


    public JSONObject getSnsPersistentCode(String authCode){
        String accessToken = getSnsToken();
        JSONObject authCodeJson = new JSONObject();
        authCodeJson.put("tmp_auth_code",authCode);
        return dingFegin.getSnsPersistentCode(accessToken,authCodeJson.toString());
    }


    public String getComToken(){
        String accessToken = stringRedisTemplate.opsForValue().get(DING_DING_COM_TOKEN_REDIS_NAME+dingDingProperties.getCorpid());
        if(accessToken ==null){
            JSONObject result = dingFegin.getComToken(dingDingProperties.getCorpid(),dingDingProperties.getCorpsecret());
            if(result.containsKey(DING_DING_ERROR_MESSAGE_NAME) && GET_TOKEN_SUCCESS.equals(result.getString(DING_DING_ERROR_MESSAGE_NAME))){
                accessToken= result.getString(DING_DING_ACCESS_TOKEN_NAME);
                stringRedisTemplate.opsForValue().set(DING_DING_COM_TOKEN_REDIS_NAME+dingDingProperties.getCorpid(),accessToken,TOKEN_TIME_OUT, TimeUnit.MINUTES);
            }
        }

        return accessToken;
    }

    public String getUserIdByUnionid(String unionid){
        String accessToken = getComToken();
        JSONObject result = dingFegin.getUseridByUnionid(accessToken,unionid);
        if(result.containsKey(DING_DING_ERROR_MESSAGE_NAME) && GET_TOKEN_SUCCESS.equals(result.getString(DING_DING_ERROR_MESSAGE_NAME))){
            return result.getString("userid");

        }
        return null;
    }

    public JSONObject getUserInfoByUserId(String userId){
        String accessToken = getComToken();
        return dingFegin.getUserInfoByUserId(accessToken,userId);
    }



    public String getUserPhoneByAuthCode(String authCode){
       JSONObject persistentCode =  getSnsPersistentCode(authCode);
       String unionid =  persistentCode.getString("unionid");
       String userId =  getUserIdByUnionid(unionid);
       JSONObject userInfo =  getUserInfoByUserId(userId);
       return userInfo.getString("mobile");
    }

}
