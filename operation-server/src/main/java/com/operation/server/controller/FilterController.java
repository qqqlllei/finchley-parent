package com.operation.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by 李雷 on 2019/1/15.
 */

@RestController
public class FilterController {

    @RequestMapping("/notifications/filters")
    public ResponseEntity<Object> notificationsFilters(){
        return ResponseEntity.ok(new ArrayList<>());
    }
}
