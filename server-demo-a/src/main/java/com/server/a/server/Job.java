//package com.server.a.server;
//
//import com.alibaba.fastjson.JSONObject;
//import com.job.lite.annotation.ElasticJobConfig;
//import com.job.lite.job.AbstractBaseDataflowJob;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * Created by 李雷 on 2019/5/16.
// */
//@Component
//@ElasticJobConfig(cron = "elastic.job.cron.clientMessageDataflowCron", jobParameter = "{'fetchNum':200,'taskType':'SENDING_MESSAGE'}",description="生产者消息清理")
//public class Job extends AbstractBaseDataflowJob {
//    @Override
//    protected List fetchJobData(JSONObject jsonObject) {
//        System.out.println("================123======================");
//        return null;
//    }
//
//    @Override
//    protected void processJobData(List list) {
//
//    }
//}
