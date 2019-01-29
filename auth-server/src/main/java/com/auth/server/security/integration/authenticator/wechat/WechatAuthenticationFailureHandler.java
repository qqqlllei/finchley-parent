package com.auth.server.security.integration.authenticator.wechat;

import com.alibaba.fastjson.JSONObject;
import com.auth.server.security.integration.AuthFailureHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李雷 on 2018/8/8.
 */
@Component
public class WechatAuthenticationFailureHandler implements AuthFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        Map<String,String> result = new HashMap<>();
        result.put("resultCode","0001");
        result.put("resultMsg","WechatAuthenticationFailureHandler");
        response.setStatus(200);
        response.getWriter().write(JSONObject.toJSONString(result));
    }
}
