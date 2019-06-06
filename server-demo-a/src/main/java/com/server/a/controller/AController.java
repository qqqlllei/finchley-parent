package com.server.a.controller;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.common.domain.ReliableMessage;
import com.server.a.server.AServer;
import com.server.a.util.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.server.a.Fegin.BServerApi;

/**
 * Created by 李雷 on 2019/1/22.
 */
@RestController
@RequestMapping("/demo")
public class AController {

    private Logger logger =LoggerFactory.getLogger(AController.class);

    @Autowired
    private UniqueId uniqueId;

    @Autowired
    private AServer aServer;


    @RequestMapping("/info")
    public ResponseEntity<String> info() throws InterruptedException {
        logger.info("AController===================info======================");
        ReliableMessage clientMessageData = new ReliableMessage();
        clientMessageData.setMessageTopic("SAVE_USER");
        JSONObject messageBody = new JSONObject();
        messageBody.put("name","李雷");
        messageBody.put("age","29");
        clientMessageData.setMessageBody(messageBody.toJSONString());
        aServer.aaa(clientMessageData);
        return ResponseEntity.ok("ok");
    }



    @RequestMapping("/mail")
    public ResponseEntity<String> mail() throws InterruptedException {
        logger.info("AController===================info======================");
        ReliableMessage clientMessageData = new ReliableMessage();
        clientMessageData.setMessageTopic("SAVE_USER");
        JSONObject messageBody = new JSONObject();
        messageBody.put("name","李雷1");
        messageBody.put("age","30");
        clientMessageData.setMessageBody(messageBody.toJSONString());
        aServer.bbb(clientMessageData);
        return ResponseEntity.ok("ok");
    }


    @RequestMapping("/message")
    public ResponseEntity<String> message() throws InterruptedException {
        logger.info("AController===================message======================");
        ReliableMessage clientMessageData = new ReliableMessage();
        clientMessageData.setMessageTopic("UPDATE_USER");
        JSONObject messageBody = new JSONObject();
        messageBody.put("name","李雷1");
        messageBody.put("age","30");
        clientMessageData.setMessageBody(messageBody.toJSONString());
        aServer.ccc(clientMessageData);
        return ResponseEntity.ok("ok");
    }




//    @RequestMapping("/save")
//    public void save(){
//        JSONObject user = new JSONObject();
//        user.put("id",uniqueId.getNextIdByApplicationName("user"));
//        user.put("name","demo");
//        aServer.save(user);
//    }

}
