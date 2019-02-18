package com.gray.gateway.configuration;

import com.alibaba.fastjson.JSONArray;
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




    @PostConstruct
    public void init() throws NacosException {

        String dateId  = currentApplicationName+"-version-"+currentApplicationVersion+".json";
        initNacosInfoAndListener(dateId);


        if("gateway-server".equals(currentApplicationName)){
            ConfigService configService = nacosConfigProperties.configServiceInstance();
            String gatewayVersionsJson = configService.getConfig("gateway-server-version-parent.json",DEFAULT_GROUP,500L);
            if(gatewayVersionsJson ==null || StringUtils.isBlank(gatewayVersionsJson)) {
                throw new NacosException(1000,"gatewayVersions is missing");
            }

            JSONArray list = JSONArray.parseArray(gatewayVersionsJson);
            for (int i=0;i<list.size();i++){
                String version = (String) list.get(i);
                initNacosInfoAndListener(currentApplicationName+"-version-"+version+".json");
            }

        }

    }


    private void initNacosInfoAndListener(String dataId) throws NacosException {
        ConfigService configService = nacosConfigProperties.configServiceInstance();
        String versionsString = configService.getConfig(dataId,DEFAULT_GROUP,2000l);
        if(versionsString ==null || StringUtils.isBlank(versionsString)) {
            throw new NacosException(1000,"server version is missing");
        }
        Cache cache = ehCacheCacheManager.getCache(SERVER_VERSION_CACHE_NAME);
        cache.put(dataId, JSONObject.parseObject(versionsString));

        configService.addListener(dataId, DEFAULT_GROUP, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                Cache cache = ehCacheCacheManager.getCache(SERVER_VERSION_CACHE_NAME);
                cache.put(dataId, JSONObject.parseObject(configInfo));
                logger.info("Listening on NacosConfigChange Event - dateId="+dataId+"-group="+DEFAULT_GROUP+"-configInfo="+configInfo );
            }
        });
    }


}
