package com.auth.server.fegin;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 李雷 on 2018/7/23.
 */
@FeignClient(name = "wechatFegin",url = "https://api.weixin.qq.com")
public interface WechatFegin {

    @RequestMapping(value = "/sns/oauth2/access_token",method = RequestMethod.GET)
    String oauth2getAccessToken(@RequestParam("appid") String appid, @RequestParam("secret") String secret,
                                @RequestParam("code") String code, @RequestParam("grant_type") String grant_type);

}
