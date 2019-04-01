package com.demo.c.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.c.service.BService;
import com.demo.c.service.UserServer;
import com.demo.c.util.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 李雷 on 2019/1/22.
 */
@RestController
@RequestMapping("/demo")
@RefreshScope
public class BController {
    private Logger logger = LoggerFactory.getLogger(BController.class);
    @Value("${aaa}")
    private String aaa;


    @Autowired
    private UserServer userServer;

    @Autowired
    private UniqueId uniqueId;




    @Autowired
    private BService bService;

    @RequestMapping("/info")
    public ResponseEntity<String> info(){

        JSONObject user = new JSONObject();
        user.put("id",uniqueId.getNextIdByApplicationName("user"));
        user.put("name","demo");
        userServer.saveUser(user);
        return ResponseEntity.ok("ok2"+aaa);

    }

}
