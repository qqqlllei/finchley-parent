package com.demo.c.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * Created by 李雷 on 2019/2/14.
 */
@Service
@RefreshScope
public class BService {

    @Value("${aaa}")
    private String aaa;

    public void info(){
        System.out.println("BService INFO"+ aaa);
    }
}
