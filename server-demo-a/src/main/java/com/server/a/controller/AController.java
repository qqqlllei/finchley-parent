package com.server.a.controller;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.common.domain.ClientMessageData;
import com.server.a.server.AServer;
import com.server.a.util.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

/**
 * Created by 李雷 on 2019/1/22.
 */
@RestController
@RequestMapping("/demo")
public class AController {

    private Logger logger =LoggerFactory.getLogger(AController.class);

//    @Autowired
//    private BServerApi bServerApi;

    @Autowired
    private AServer aServer;


    @Value("${topic.saveUser}")
    private String saveUserTopic;


    @RequestMapping("/info")
    public ResponseEntity<String> info() throws InterruptedException {
        logger.info("AController===================info======================");
        ClientMessageData clientMessageData = new ClientMessageData();
        clientMessageData.setMessageTopic(saveUserTopic);
        JSONObject messageBody = new JSONObject();
        messageBody.put("name","李雷");
        messageBody.put("age","29");
        clientMessageData.setMessageBody(messageBody.toJSONString());
        aServer.aaa(clientMessageData);
        return ResponseEntity.ok("ok");
    }

}
