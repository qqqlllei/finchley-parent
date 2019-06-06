package com.server.a.server;

import com.alibaba.fastjson.JSONObject;
import com.reliable.message.client.annotation.MessageProducer;
import com.reliable.message.common.domain.ReliableMessage;
import com.reliable.message.common.enums.MessageSendTypeEnum;
import com.server.a.util.UniqueId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.alibaba.fescar.spring.annotation.GlobalTransactional;
//import com.server.a.Fegin.BServerApi;

/**
 * Created by 李雷 on 2019/3/18.
 */
@Service
public class AServer {

    @Autowired
    private UserServer userServer;

//    @Autowired
//    private BServerApi bServerApi;

    @Autowired
    private UniqueId uniqueId;

    @MessageProducer
    @Transactional
    public void aaa(ReliableMessage messageData){
        System.out.println("=====================");
        JSONObject user = new JSONObject();
        user.put("id",uniqueId.getNextIdByApplicationName("user"));
        user.put("name","demo");
        userServer.saveUser(user);


    }

    @MessageProducer(sendType= MessageSendTypeEnum.SAVE_AND_SEND)
    public void bbb(ReliableMessage clientMessageData) {
    }


    @MessageProducer(sendType= MessageSendTypeEnum.DIRECT_SEND)
    public void ccc(ReliableMessage clientMessageData) {
    }


//    @GlobalTransactional(timeoutMills = 6000000)
//    public void save(JSONObject user){
//        userServer.saveUser(user);
//        bServerApi.save(user);
//
//        System.out.println(123);
//    }
}
