package com.server.a.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 李雷 on 2019/3/20.
 */
public interface UserMapper {

    void saveUser(JSONObject user);

}
