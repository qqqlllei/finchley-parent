package com.gray.gateway.configuration;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.gray.gateway.util.GrayConstant;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;

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

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.application.name}")
    private String currentApplicationName;

    @Value("${info.version}")
    private String currentApplicationVersion;


    @Value("${gray.configuration.local.enable}")
    private boolean configurationLocalEnable;

    @PostConstruct
    public void init() throws NacosException {

        String dateName  = currentApplicationName+"-"+ GrayConstant.SERVER_GRAY_CACHE_NAME+"-"+currentApplicationVersion;
        if(configurationLocalEnable){
            initLocalConfigInfoToCache(dateName);
        }else {
            initNacosInfoAndListener(dateName);
        }

    }


    private void initNacosInfoAndListener(String dateName) throws NacosException {
        ConfigService configService = nacosConfigProperties.configServiceInstance();
        String dataId = dateName+GrayConstant.SERVER_GRAY_FILE_EXTENSION;
        String grayString = configService.getConfig(dataId,DEFAULT_GROUP,GrayConstant.GET_NACOS_CONFIG_TIMEOUT);

        initConfigInfoToCache(grayString,dateName);

        configService.addListener(dataId, DEFAULT_GROUP, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {

                try {
                    initConfigInfoToCache(configInfo,dateName);
                } catch (NacosException e) {
                    e.printStackTrace();
                }
                logger.info("Listening on NacosConfigChange Event - dateId="+dataId+"-group="+DEFAULT_GROUP+"-configInfo="+configInfo );
            }
        });
    }


    private void initLocalConfigInfoToCache(String dateName) throws NacosException {
        InputStream inputStream ;
        String filePath = applicationContext.getEnvironment().resolvePlaceholders(GrayConstant.SERVER_GRAY_LOCAL_CONFIG_INFO_PATH);
        try {
            inputStream = applicationContext.getResource(filePath).getInputStream();
            String localConfigInfo = IOUtils.toString(inputStream,GrayConstant.SERVER_GRAY_CHARSET_UTF8);

            initConfigInfoToCache(localConfigInfo,dateName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initConfigInfoToCache(String configInfo,String dateName) throws NacosException {
        if(configInfo ==null || StringUtils.isBlank(configInfo)) {
            throw new NacosException(1000,"server gray info is missing");
        }
        JSONObject gray = JSONObject.parseObject(configInfo);
        JSONObject grayVersion = gray.getJSONObject(GrayConstant.SERVER_GRAY_VERSION_NAME);

        if(grayVersion == null ){
            throw new NacosException(1000,"server gray version  is missing");
        }
        JSONObject grayIp = gray.getJSONObject(GrayConstant.SERVER_GRAY_IP_NAME);

        Cache cache = ehCacheCacheManager.getCache(GrayConstant.SERVER_GRAY_CACHE_NAME);
        cache.put(dateName+GrayConstant.SERVER_GRAY_VERSION_NAME, grayVersion);
        cache.put(dateName+GrayConstant.SERVER_GRAY_IP_NAME, grayIp);
    }


}
