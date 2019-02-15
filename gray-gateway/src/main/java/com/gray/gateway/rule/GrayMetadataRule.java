package com.gray.gateway.rule;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;

import java.util.List;

public class GrayMetadataRule extends PredicateBasedRule {

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
			String versions = configService.getConfig("auth-server-version-1.0.1","DEFAULT_GROUP",2000l);
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