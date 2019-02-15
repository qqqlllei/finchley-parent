package com.server.b.controller;

import com.server.b.service.BService;
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

    @Value("${aaa}")
    private String aaa;




    @Autowired
    private BService bService;

    @RequestMapping("/info")
    public ResponseEntity<String> info(){

        System.out.println("BController===================info======================");
        bService.info();
        return ResponseEntity.ok("ok"+aaa);

    }

}
