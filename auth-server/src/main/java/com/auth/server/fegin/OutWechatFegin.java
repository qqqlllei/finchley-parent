package com.auth.server.fegin;

import com.auth.server.security.vo.AuthUser;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 李雷 on 2018/7/24.
 */
@FeignClient("out-wechat")
public interface OutWechatFegin extends LoginAbstractFegin{

    @Override
    @RequestMapping(value="/channelStaff/findUserByOpenId")
    AuthUser findUserById(@RequestParam("openId") String openId);
}
