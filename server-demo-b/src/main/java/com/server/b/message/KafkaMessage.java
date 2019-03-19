package com.server.b.message;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.model.domain.ClientMessageData;
import com.server.b.topic.TopicConst;
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


    @KafkaListener(topics = TopicConst.SAVE_USER)
    public void saveUser(String message){
        ClientMessageData messageData = JSONObject.parseObject(message, ClientMessageData.class);
        messageServer.saveUser(messageData);
    }

}
