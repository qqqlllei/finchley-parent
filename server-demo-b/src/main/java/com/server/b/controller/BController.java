package com.server.b.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 李雷 on 2019/1/22.
 */
@RestController
@RequestMapping("/demo")
public class BController {


    @RequestMapping("/info")
    public ResponseEntity<String> info(){

        System.out.println("BController===================info======================");
        return ResponseEntity.ok("ok");
    }

}
