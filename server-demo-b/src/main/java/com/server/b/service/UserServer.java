package com.server.b.service;

import com.alibaba.fastjson.JSONObject;
import com.server.b.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 李雷 on 2019/3/20.
 */

@Service
public class UserServer {

    @Autowired
    private UserMapper userMapper;


    @Transactional
    public void saveUser(JSONObject user){
        this.userMapper.saveUser(user);
        int i=1/0;
    }
}
