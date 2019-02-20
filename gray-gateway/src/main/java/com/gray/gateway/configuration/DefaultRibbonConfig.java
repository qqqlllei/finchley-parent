package com.gray.gateway.configuration;


import com.gray.gateway.rule.GrayMetadataRule;
import com.gray.gateway.service.NacosConfigToCacheService;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 李雷 on 2019/1/24.
 */
@Configuration
public class DefaultRibbonConfig {

    @Autowired
    private NacosConfigToCacheService nacosConfigToCacheService;

    @Bean
    public IRule ribbonRule(){
        GrayMetadataRule grayMetadataRule =  new GrayMetadataRule();
        grayMetadataRule.getNacosServerPredicate().setNacosConfigToCacheService(nacosConfigToCacheService);
        return grayMetadataRule;
    }
}
