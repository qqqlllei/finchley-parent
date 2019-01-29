package com.auth.server.fegin;

import com.auth.server.security.vo.AuthUser;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by 李雷 on 2018/7/10.
 */
@FeignClient(name = "user-server",url = "${user-server.url}")
@RequestMapping("/riskUserLogin")
public interface UserFegin {

    @RequestMapping(value="/findUserByUsername")
    AuthUser findUserByUsername(@RequestParam("name") String name);

    @RequestMapping(value = "/queryLoginUser", method = RequestMethod.POST)
    Map<String,Object> queryLoginUser(@RequestBody Map<String, Object> paramMap);

    @RequestMapping(value="/findUserByPhoneNumber")
    AuthUser findUserByPhoneNumber(@RequestParam("phone") String phone);


}
