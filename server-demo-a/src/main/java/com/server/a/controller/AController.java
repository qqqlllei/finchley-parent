package com.server.a.controller;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.client.util.UUIDUtil;
import com.reliable.message.model.domain.ClientMessageData;
import com.server.a.Fegin.BServerApi;
import com.server.a.server.AServer;
import com.server.a.util.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UniqueId uniqueId;


    @RequestMapping("/info")
    public ResponseEntity<String> info() throws InterruptedException {

//        Thread.sleep(20000l);
        logger.info("AController===================info======================");
        ClientMessageData clientMessageData = new ClientMessageData();
        clientMessageData.setMessageTopic("SAVE_USER");
        JSONObject messageBody = new JSONObject();
        messageBody.put("name","李雷");
        messageBody.put("age","29");
        clientMessageData.setMessageBody(messageBody.toJSONString());
        aServer.aaa(clientMessageData);
        return ResponseEntity.ok("ok");
    }

}
