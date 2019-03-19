package com.server.a.server;

import com.reliable.message.client.annotation.MessageProducerStore;
import com.reliable.message.model.domain.ClientMessageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 李雷 on 2019/3/18.
 */
@Service
public class AServer {


    @MessageProducerStore
    @Transactional
    public void aaa(ClientMessageData messageData){
        System.out.println("=====================");
    }
}
