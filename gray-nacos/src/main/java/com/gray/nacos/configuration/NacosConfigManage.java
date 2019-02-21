package com.gray.nacos.configuration;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.gray.nacos.service.NacosConfigToCacheService;
import com.gray.nacos.util.GrayConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;

import javax.annotation.PostConstruct;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

/**
 * Created by 李雷 on 2019/2/20.
 */
public class NacosConfigManage {

    private static final Logger logger = LoggerFactory.getLogger(NacosConfigManage.class);

    @Value("${spring.application.name}")
    private String currentApplicationName;

    @Value("${info.version}")
    private String currentApplicationVersion;

    @Value("${gray.configuration.local.enable:false}")
    private boolean configurationLocalEnable;

    private String DATA_NAME;


    @Autowired
    private NacosConfigProperties nacosConfigProperties;


    private NacosConfigToCacheService nacosConfigToCacheService;

    @PostConstruct
    public void init() throws NacosException {

        DATA_NAME  = currentApplicationName+"-"+ GrayConstant.SERVER_GRAY_CACHE_NAME+"-"+currentApplicationVersion;
        if(!configurationLocalEnable){
            initNacosInfoAndListener(DATA_NAME);
        }

    }



    public void initNacosInfoAndListener(String dateName) throws NacosException {
        ConfigService configService = nacosConfigProperties.configServiceInstance();
        String dataId = dateName+ GrayConstant.SERVER_GRAY_FILE_EXTENSION;
        String grayString = configService.getConfig(dataId,DEFAULT_GROUP,GrayConstant.GET_NACOS_CONFIG_TIMEOUT);

        nacosConfigToCacheService.initConfigInfoToCache(grayString,dateName);

        configService.addListener(dataId, DEFAULT_GROUP, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {

                try {
                    nacosConfigToCacheService.initConfigInfoToCache(configInfo,dateName);
                } catch (NacosException e) {
                    e.printStackTrace();
                }
                logger.info("Listening on NacosConfigChange Event - dateId="+dataId+"-group="+DEFAULT_GROUP+"-configInfo="+configInfo );
            }
        });
    }


    public void setNacosConfigToCacheService(NacosConfigToCacheService nacosConfigToCacheService) {
        this.nacosConfigToCacheService = nacosConfigToCacheService;
    }
}
