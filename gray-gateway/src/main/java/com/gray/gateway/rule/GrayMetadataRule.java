package com.gray.gateway.rule;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Optional;
import com.gray.gateway.util.GrayConstant;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

public class GrayMetadataRule extends PredicateBasedRule {

	@Value("${spring.application.name}")
	private String currentApplicationName;

	@Value("${info.version}")
	private String currentApplicationVersion;

	@Autowired
	private NacosConfigProperties nacosConfigProperties;

	private String DATA_ID ;
	private String DATA_NAME;

	@PostConstruct
	public void init(){
		DATA_NAME=currentApplicationName+"-"+ GrayConstant.SERVER_GRAY_CACHE_NAME+"-"+currentApplicationVersion;
		DATA_ID= DATA_NAME+ GrayConstant.SERVER_GRAY_FILE_EXTENSION;
	}

	@Autowired
	private CacheManager cacheManager;

	private ZoneAvoidancePredicate zoneAvoidancePredicate;

	public GrayMetadataRule(){
		super();
		zoneAvoidancePredicate = new ZoneAvoidancePredicate(this,null);
	}


	@Override
	public Server choose(Object key) {

		BaseLoadBalancer loadBalancer = (BaseLoadBalancer) getLoadBalancer();

		Cache cache = cacheManager.getCache(GrayConstant.SERVER_GRAY_CACHE_NAME);
		List<Server> eligibleServers = getPredicate().getEligibleServers(loadBalancer.getAllServers(), key);

		JSONObject grayVersion = cache.get(DATA_NAME+GrayConstant.SERVER_GRAY_VERSION_NAME,JSONObject.class);
		JSONObject grayIp = cache.get(DATA_NAME+GrayConstant.SERVER_GRAY_IP_NAME,JSONObject.class);
		if(grayVersion==null || grayIp == null) {
			try {
				ConfigService configService = nacosConfigProperties.configServiceInstance();
				String grayString = configService.getConfig(DATA_ID,DEFAULT_GROUP,GrayConstant.GET_NACOS_CONFIG_TIMEOUT);
				JSONObject gray = JSONObject.parseObject(grayString);
				grayVersion = gray.getJSONObject(GrayConstant.SERVER_GRAY_VERSION_NAME);
				grayIp = gray.getJSONObject(GrayConstant.SERVER_GRAY_IP_NAME);
			} catch (NacosException e) {
				e.printStackTrace();
			}
		}
		JSONArray versions = grayVersion.getJSONArray(loadBalancer.getName());
		filterVersion(eligibleServers,versions);
		filterIp(eligibleServers,grayIp);
		return originChoose(eligibleServers,key);
	}




	@Override
	public AbstractServerPredicate getPredicate() {
		return zoneAvoidancePredicate;
	}


	private Server originChoose(List<Server> noMetaServerList, Object key) {
		Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(noMetaServerList, key);
		if (server.isPresent()) {
			return server.get();
		} else {
			return null;
		}
	}



	private void filterIp(List<Server> eligibleServers, JSONObject grayIp) {
		Iterator<Server> iterator = eligibleServers.iterator();
		JSONArray whiteList = grayIp.getJSONArray(GrayConstant.SERVER_GRAY_WHITE_LIST_NAME);
		JSONArray blackList = grayIp.getJSONArray(GrayConstant.SERVER_GRAY_BLACK_LIST_NAME);
		while (iterator.hasNext()){
			NacosServer nacosServer = (NacosServer) iterator.next();
			String host = nacosServer.getHost();
			if(whiteList !=null && whiteList.size()>0 && !whiteList.contains(host)){
				iterator.remove();
			}

			if(blackList!=null && blackList.size()>0 && blackList.contains(host)){
				iterator.remove();
			}

		}
	}


	private void filterVersion(List<Server> eligibleServers,JSONArray versions){
		Iterator<Server> iterator = eligibleServers.iterator();
		while (iterator.hasNext()){
			NacosServer nacosServer = (NacosServer) iterator.next();
			String version = nacosServer.getMetadata().get(GrayConstant.SERVER_GRAY_VERSION_NAME);
			if(!versions.contains(version)){
				iterator.remove();
			}
		}
	}
}