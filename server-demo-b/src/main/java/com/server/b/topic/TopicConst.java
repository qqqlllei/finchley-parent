package com.server.b.topic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 李雷 on 2019/3/19.
 */
@Component
public class TopicConst {


    public static final String SERVER_NAME="DEMO-B";
    public static String version;
    public static final String SAVE_USER = "SAVE_USER_"+SERVER_NAME+"_"+version;
    @Value("${info.version}")
    public static void setVersion(String version) {
        TopicConst.version = version;
    }
}
