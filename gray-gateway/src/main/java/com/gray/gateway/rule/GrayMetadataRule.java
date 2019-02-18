package com.gray.gateway.rule;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Optional;
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

public class GrayMetadataRule extends PredicateBasedRule {


	private static final String SERVER_VERSION_CACHE_NAME="version";
	@Value("${spring.application.name}")
	private String currentApplicationName;

	@Value("${info.version}")
	private String currentApplicationVersion;

	@Autowired
	private NacosConfigProperties nacosConfigProperties;

	private String DATA_ID ;

	@PostConstruct
	public void init(){
		DATA_ID= currentApplicationName+"-version-"+currentApplicationVersion+".json";
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

		Cache cache = cacheManager.getCache(SERVER_VERSION_CACHE_NAME);
		JSONObject servers = cache.get(DATA_ID,JSONObject.class);
		if(servers==null) {
			try {
				ConfigService configService = nacosConfigProperties.configServiceInstance();
				String versionsString = configService.getConfig(DATA_ID,"DEFAULT_GROUP",2000l);
				servers = JSONObject.parseObject(versionsString);
			} catch (NacosException e) {
				e.printStackTrace();
			}
		}


		JSONArray versions = servers.getJSONArray(loadBalancer.getName());

		List<Server> eligibleServers = getPredicate().getEligibleServers(loadBalancer.getAllServers(), key);
		Iterator<Server> iterator = eligibleServers.iterator();
		while (iterator.hasNext()){
			NacosServer nacosServer = (NacosServer) iterator.next();
			String version = nacosServer.getMetadata().get(SERVER_VERSION_CACHE_NAME);
			if(!versions.contains(version)){
				iterator.remove();
			}
		}
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
}