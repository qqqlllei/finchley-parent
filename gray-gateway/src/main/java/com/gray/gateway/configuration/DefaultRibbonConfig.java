package com.gray.gateway.configuration;


import com.gray.gateway.rule.GrayMetadataRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 李雷 on 2019/1/24.
 */
@Configuration
public class DefaultRibbonConfig {

    @Bean
    public IRule ribbonRule(){
        return new GrayMetadataRule();
    }
}
