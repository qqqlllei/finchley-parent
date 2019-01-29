package com.auth.server.fegin;


import com.auth.server.security.vo.AuthUser;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 李雷 on 2018/8/13.
 */

@FeignClient("customer-wechat")
public interface CustomerWechatFegin extends LoginAbstractFegin{

    @Override
    @RequestMapping(value="/api/user/findUserByOpenId")
    AuthUser findUserById(@RequestParam("openId") String openId);


    @Override
    @RequestMapping(value="/api/user/findUserByPhone")
    AuthUser findUserByPhone(@RequestParam("phone") String phone);


    @RequestMapping(value="/api/user/loginSuccess")
    void loginSuccess(@RequestParam("openId") String openId, @RequestParam("phone") String phone);

}
