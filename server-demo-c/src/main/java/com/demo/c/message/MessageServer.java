package com.demo.c.message;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.client.annotation.MessageConsumer;
import com.reliable.message.common.domain.ServerMessageData;
import com.demo.c.service.UserServer;
import com.demo.c.util.UniqueId;
import com.reliable.message.common.dto.MessageData;
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


    @MessageConsumer
    @Transactional
    public void saveUser(MessageData serverMessageData){
        JSONObject user = new JSONObject();
        user.put("id",uniqueId.getNextIdByApplicationName("user"));
        user.put("name","demo");
        userServer.saveUser(user);
        System.out.println("=========consumer============"+JSONObject.toJSONString(serverMessageData));
    }
}
