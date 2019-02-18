package com.gray.gateway.rule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrayMetadataRule extends PredicateBasedRule {

	@Value("${spring.application.name}")
	private String currentApplicationName;

	@Autowired
	private NacosConfigProperties nacosConfigProperties;


	private ZoneAvoidancePredicate zoneAvoidancePredicate;

	public GrayMetadataRule(){
		super();
		zoneAvoidancePredicate = new ZoneAvoidancePredicate(this,null);
	}


	@Override
	public Server choose(Object key) {

		BaseLoadBalancer loadBalancer = (BaseLoadBalancer) getLoadBalancer();

		try {
			ConfigService configService = nacosConfigProperties.configServiceInstance();
			String versionsString = configService.getConfig(currentApplicationName+"-version-1.0.1.json","DEFAULT_GROUP",2000l);
			JSONObject servers = JSONObject.parseObject(versionsString);
			JSONArray versions = servers.getJSONArray(loadBalancer.getName());

			System.out.println(versions);


		} catch (NacosException e) {
			e.printStackTrace();
		}

		List<Server> eligibleServers = getPredicate().getEligibleServers(loadBalancer.getAllServers(), key);
		System.out.println("====================serverList"+eligibleServers.size());
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