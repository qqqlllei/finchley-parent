package com.server.a.server;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.client.annotation.MessageProducerStore;
import com.reliable.message.model.domain.ClientMessageData;
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


    @MessageProducerStore
    @Transactional
    public void aaa(ClientMessageData messageData){
        System.out.println("=====================");
        JSONObject user = new JSONObject();
        user.put("id",uniqueId.getNextIdByApplicationName("user"));
        user.put("name","demo");
        userServer.saveUser(user);
    }
}
