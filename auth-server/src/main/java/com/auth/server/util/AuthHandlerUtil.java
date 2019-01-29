package com.auth.server.util;



import com.auth.server.security.AuthClientProperties;
import com.auth.server.security.constants.SecurityConstant;

import java.util.List;
import java.util.Map;

/**
 * Created by 李雷 on 2018/9/14.
 */
public class AuthHandlerUtil {



    public static String getSuccessHandlerByType(String type, Map<String,Object> additionalInformation, AuthClientProperties authClientProperties){
        String key = type+ SecurityConstant.AUTH_SUCCESS_HANDLER_POSTFIX;
        if(additionalInformation.containsKey(key)){
            return String.valueOf(additionalInformation.get(key));
        }

        List<Map<String, String>> handlers =  authClientProperties.getHandlers();
        for (Map<String,String> map: handlers) {
            if(map.get(SecurityConstant.AUTH_TYPE_PARM_NAME).equals(type)){
                return map.get(SecurityConstant.AUTH_SUCCESS_HANDLER);
            }
        }

        return SecurityConstant.AUTH_DEFAULT_SUCCESS_HANDLER;
    }


    public static String getFailHandlerByType(String type, Map<String,Object> additionalInformation, AuthClientProperties authClientProperties){
        String key = type+ SecurityConstant.AUTH_FAILURE_HANDLER;
        if(additionalInformation.containsKey(key)){
            return String.valueOf(additionalInformation.get(key));
        }

        List<Map<String, String>> handlers =  authClientProperties.getHandlers();
        for (Map<String,String> map: handlers) {
            if(map.get(SecurityConstant.AUTH_TYPE_PARM_NAME).equals(type)){
                return map.get(SecurityConstant.AUTH_FAILURE_HANDLER);
            }
        }

        return SecurityConstant.AUTH_DEFAULT_FAILURE_HANDLER;
    }
}
