package com.gray.nacos.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.gray.nacos.util.GrayConstant;
import com.netflix.loadbalancer.Server;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by 李雷 on 2019/2/19.
 */
public class NacosConfigToCacheService {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private EhCacheCacheManager ehCacheCacheManager;

    @Value("${spring.application.name}")
    private String currentApplicationName;

    @Value("${info.version}")
    private String currentApplicationVersion;


    @Value("${gray.configuration.local.enable:false}")
    private boolean configurationLocalEnable;

    private String DATA_NAME;

    @PostConstruct
    public void init() throws NacosException {

        DATA_NAME  = currentApplicationName+"-"+ GrayConstant.SERVER_GRAY_CACHE_NAME+"-"+currentApplicationVersion;
        if(configurationLocalEnable){
            initLocalConfigInfoToCache(DATA_NAME);
        }

    }





    public void initLocalConfigInfoToCache(String dateName) throws NacosException {
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


    public void initConfigInfoToCache(String configInfo,String dateName) throws NacosException {
        if(configInfo ==null || StringUtils.isBlank(configInfo)) {
            throw new NacosException(1000,"server gray info is missing");
        }
        JSONObject gray = JSONObject.parseObject(configInfo);
        Set<String> servers = gray.keySet();
        for (String key:servers){
            JSONObject server = gray.getJSONObject(key);
            JSONArray grayVersion = server.getJSONArray(GrayConstant.SERVER_GRAY_VERSION_NAME);
            if(grayVersion == null || grayVersion.size()==0){
                throw new NacosException(1000,"server ["+key+"] gray version  is missing");
            }
            JSONObject grayIp = server.getJSONObject(GrayConstant.SERVER_GRAY_IP_NAME);
            Cache cache = ehCacheCacheManager.getCache(GrayConstant.SERVER_GRAY_CACHE_NAME);
            cache.put(dateName+key+GrayConstant.SERVER_GRAY_VERSION_NAME, grayVersion);
            cache.put(dateName+key+GrayConstant.SERVER_GRAY_IP_NAME, grayIp);
        }
    }


    public boolean filterVersion(Server server){
        NacosServer nacosServer = (NacosServer) server;
        String version = nacosServer.getMetadata().get(GrayConstant.SERVER_GRAY_VERSION_NAME);
        if(!getVersions(server).contains(version)){
            return false;
        }
        return true;
    }

    public boolean filterIp(Server server){
        NacosServer nacosServer = (NacosServer) server;
        JSONObject grayIp =  getGrapIp(server);
        JSONArray whiteList = grayIp.getJSONArray(GrayConstant.SERVER_GRAY_WHITE_LIST_NAME);
        JSONArray blackList = grayIp.getJSONArray(GrayConstant.SERVER_GRAY_BLACK_LIST_NAME);

        String host = nacosServer.getHost();
        if(whiteList !=null && whiteList.size()>0 && !whiteList.contains(host)){
            return false;
        }

        if(blackList!=null && blackList.size()>0 && blackList.contains(host)){
            return false;
        }

        return true;
    }


    public JSONArray getVersions(Server server){
        NacosServer nacosServer = (NacosServer) server;
        Cache cache = ehCacheCacheManager.getCache(GrayConstant.SERVER_GRAY_CACHE_NAME);
        return cache.get(DATA_NAME+nacosServer.getInstance().getServiceName()
                + GrayConstant.SERVER_GRAY_VERSION_NAME,JSONArray.class);

    }

    public JSONObject getGrapIp(Server server){

        NacosServer nacosServer = (NacosServer) server;

        Cache cache = ehCacheCacheManager.getCache(GrayConstant.SERVER_GRAY_CACHE_NAME);
        return cache.get(DATA_NAME+nacosServer.getInstance().getServiceName()
                +GrayConstant.SERVER_GRAY_IP_NAME,JSONObject.class);
    }


}
