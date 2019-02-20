package com.server.a.controller;

import com.server.a.Fegin.BServerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 李雷 on 2019/1/22.
 */
@RestController
@RequestMapping("/demo")
public class AController {

    private Logger logger =LoggerFactory.getLogger(AController.class);

    @Autowired
    private BServerApi bServerApi;


    @RequestMapping("/info")
    public ResponseEntity<String> info() throws InterruptedException {

//        Thread.sleep(20000l);
        logger.info("AController===================info======================");
        bServerApi.info();
        return ResponseEntity.ok("ok");
    }

}
