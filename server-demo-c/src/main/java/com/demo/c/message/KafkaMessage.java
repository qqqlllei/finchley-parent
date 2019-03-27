package com.demo.c.message;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.common.domain.ServerMessageData;
import com.reliable.message.common.dto.MessageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by 李雷 on 2019/3/19.
 */
@Component
public class KafkaMessage {

    @Autowired
    private MessageServer messageServer;


    @KafkaListener(topics ={"#{'${topic.saveUser}'.split(',')}"})
    public void saveUser(String message){
        MessageData messageData = JSONObject.parseObject(message, MessageData.class);
        messageServer.saveUser(messageData);
    }


}
