package com.demo.c.dao;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by 李雷 on 2019/3/20.
 */
public interface UserMapper {

    void saveUser(JSONObject user);

}
