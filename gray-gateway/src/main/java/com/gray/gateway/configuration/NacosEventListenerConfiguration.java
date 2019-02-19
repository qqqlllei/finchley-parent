package com.gray.gateway.configuration;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.gray.gateway.util.GrayConstant;
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

    @Value("${spring.application.name}")
    private String currentApplicationName;

    @Value("${info.version}")
    private String currentApplicationVersion;

    @PostConstruct
    public void init() throws NacosException {

        String dateName  = currentApplicationName+"-"+ GrayConstant.SERVER_GRAY_CACHE_NAME+"-"+currentApplicationVersion;
        initNacosInfoAndListener(dateName);
    }


    private void initNacosInfoAndListener(String dateName) throws NacosException {
        ConfigService configService = nacosConfigProperties.configServiceInstance();
        String dataId = dateName+GrayConstant.SERVER_GRAY_FILE_EXTENSION;
        String grayString = configService.getConfig(dataId,DEFAULT_GROUP,2000l);
        if(grayString ==null || StringUtils.isBlank(grayString)) {
            throw new NacosException(1000,"server gray is missing");
        }

        JSONObject gray = JSONObject.parseObject(grayString);
        JSONObject grayVersion = gray.getJSONObject(GrayConstant.SERVER_GRAY_VERSION_NAME);

        if(grayVersion == null ){
            throw new NacosException(1000,"server gray version  is missing");
        }

        JSONObject grayIp = gray.getJSONObject(GrayConstant.SERVER_GRAY_IP_NAME);

        Cache cache = ehCacheCacheManager.getCache(GrayConstant.SERVER_GRAY_CACHE_NAME);
        cache.put(dateName+GrayConstant.SERVER_GRAY_VERSION_NAME, grayVersion);
        cache.put(dateName+GrayConstant.SERVER_GRAY_IP_NAME, grayIp);

        configService.addListener(dataId, DEFAULT_GROUP, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                Cache cache = ehCacheCacheManager.getCache(GrayConstant.SERVER_GRAY_CACHE_NAME);
                JSONObject grayChange = JSONObject.parseObject(configInfo);
                JSONObject grayChangeVersion = grayChange.getJSONObject(GrayConstant.SERVER_GRAY_VERSION_NAME);
                JSONObject grayChangeIp = grayChange.getJSONObject(GrayConstant.SERVER_GRAY_IP_NAME);
                cache.put(dateName+GrayConstant.SERVER_GRAY_VERSION_NAME, grayChangeVersion);
                cache.put(dateName+GrayConstant.SERVER_GRAY_IP_NAME, grayChangeIp);
                logger.info("Listening on NacosConfigChange Event - dateId="+dataId+"-group="+DEFAULT_GROUP+"-configInfo="+configInfo );
            }
        });
    }


}
