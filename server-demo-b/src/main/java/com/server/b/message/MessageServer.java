package com.server.b.message;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.client.annotation.MessageConsumerStore;
import com.reliable.message.model.domain.ClientMessageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 李雷 on 2019/3/19.
 */
@Service
public class MessageServer {



    @MessageConsumerStore
    @Transactional
    public void saveUser(ClientMessageData clientMessageData){


        System.out.println("=========consumer============"+JSONObject.toJSONString(clientMessageData));


    }
}
