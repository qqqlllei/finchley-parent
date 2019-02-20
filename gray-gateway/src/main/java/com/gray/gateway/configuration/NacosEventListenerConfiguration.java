package com.gray.gateway.configuration;

import com.gray.gateway.service.NacosConfigToCacheService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 李雷 on 2019/2/18.
 */
@Configuration
public class NacosEventListenerConfiguration {


    @Bean
    public NacosConfigManage nacosConfigManage(){
        NacosConfigManage nacosConfigManage = new NacosConfigManage();
        nacosConfigManage.setNacosConfigToCacheService(nacosConfigService());
        return nacosConfigManage;
    }

    @Bean
    public NacosConfigToCacheService nacosConfigService(){
        return new NacosConfigToCacheService();
    }
}
