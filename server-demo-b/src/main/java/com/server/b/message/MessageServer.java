package com.server.b.message;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.client.annotation.MessageConsumerStore;
import com.reliable.message.model.domain.ClientMessageData;
import com.reliable.message.model.domain.ServerMessageData;
import com.server.b.service.UserServer;
import com.server.b.util.UniqueId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 李雷 on 2019/3/19.
 */
@Service
public class MessageServer {


    @Autowired
    private UserServer userServer;

    @Autowired
    private UniqueId uniqueId;


    @MessageConsumerStore
    @Transactional
    public void saveUser(ServerMessageData serverMessageData){
        JSONObject user = new JSONObject();
        user.put("id",uniqueId.getNextIdByApplicationName("user"));
        user.put("name","demo");
        userServer.saveUser(user);
        System.out.println("=========consumer============"+JSONObject.toJSONString(serverMessageData));


    }
}
