package com.server.a.server;

import com.alibaba.fastjson.JSONObject;
import com.server.a.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 李雷 on 2019/3/20.
 */

@Service
public class UserServer {

    @Autowired
    private UserMapper userMapper;

    public void saveUser(JSONObject user){
        this.userMapper.saveUser(user);
    }
}
