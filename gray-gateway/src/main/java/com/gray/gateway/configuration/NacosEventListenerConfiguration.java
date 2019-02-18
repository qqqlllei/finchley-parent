package com.gray.gateway.configuration;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

/**
 * Created by 李雷 on 2019/2/18.
 */
@Configuration
public class NacosEventListenerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(NacosEventListenerConfiguration.class);

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Autowired
    private EhCacheCacheManager ehCacheCacheManager;

    private static final String SERVER_VERSION_CACHE_NAME="version";


    @Value("${spring.application.name}")
    private String currentApplicationName;

    @Value("${info.version}")
    private String currentApplicationVersion;

    private String DATA_ID ;


    @PostConstruct
    public void init() throws NacosException {

        DATA_ID = currentApplicationName+"-version-"+currentApplicationVersion+".json";
        ConfigService configService = nacosConfigProperties.configServiceInstance();
        String versionsString = configService.getConfig(DATA_ID,DEFAULT_GROUP,2000l);
        if(versionsString ==null || StringUtils.isBlank(versionsString)) {
            throw new NacosException(1000,"server version is missing");
        }
        Cache cache = ehCacheCacheManager.getCache(SERVER_VERSION_CACHE_NAME);
        cache.put(DATA_ID, JSONObject.parseObject(versionsString));


        configService.addListener(DATA_ID, DEFAULT_GROUP, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                Cache cache = ehCacheCacheManager.getCache(SERVER_VERSION_CACHE_NAME);
                cache.put(DATA_ID, JSONObject.parseObject(configInfo));
                logger.info("Listening on NacosConfigChange Event - dateId="+DATA_ID+"-group="+DEFAULT_GROUP+"-configInfo="+configInfo );
            }
        });

    }


}
