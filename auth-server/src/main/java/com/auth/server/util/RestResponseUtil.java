package com.auth.server.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 李雷 on 2018/7/13.
 */
public class RestResponseUtil {
    private static String CONTENT_TYPE="application/json;charset=UTF-8";

    private static String SMS_CODE_ERROR="4000";
    private static String SMS_CODE_ERROR_MSG="验证码错误或已过期";


    private static String WECHAT_OAUTH_ERROR="4001";
    private static String WECHAT_OAUTH_ERROR_MSG="微信授权错误";


    private static String USERNAME_PASSWORD_OAUTH_ERROR="4002";
    private static String USERNAME_PASSWORD_OAUTH_ERROR_MSG="用户名密码错误";


    private static void authErrorResponse(String resultCode, String resultMsg, HttpServletResponse response){

        JSONObject result = new JSONObject();
        result.put("resultCode",resultCode);
        result.put("resultMsg",resultMsg);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void smsCodeError(HttpServletResponse response){
        authErrorResponse(SMS_CODE_ERROR,SMS_CODE_ERROR_MSG,response);
    }

    public static void wechatAuthError(HttpServletResponse response){
        authErrorResponse(WECHAT_OAUTH_ERROR,WECHAT_OAUTH_ERROR_MSG,response);
    }


    public static void usernamePasswordAuthError(HttpServletResponse response){
        authErrorResponse(USERNAME_PASSWORD_OAUTH_ERROR,USERNAME_PASSWORD_OAUTH_ERROR_MSG,response);
    }
}
