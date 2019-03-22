package com.server.a.server;

import com.alibaba.fastjson.JSONObject;

import com.reliable.message.client.annotation.MessageProducer;
import com.reliable.message.common.domain.ClientMessageData;
import com.server.a.util.UniqueId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 李雷 on 2019/3/18.
 */
@Service
public class AServer {

    @Autowired
    private UserServer userServer;

    @Autowired
    private UniqueId uniqueId;


    @MessageProducer
    @Transactional
    public void aaa(ClientMessageData messageData){
        System.out.println("=====================");
        JSONObject user = new JSONObject();
        user.put("id",uniqueId.getNextIdByApplicationName("user"));
        user.put("name","demo");
        userServer.saveUser(user);
    }
}
