package com.gray.core.rule;

import com.google.common.base.Optional;
import com.gray.core.util.ThreadLocalContext;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrayMetadataRule extends ZoneAvoidanceRule {
    public static final String META_DATA_KEY_VERSION = "version";

	public static final String IS_GRAY_FLAG = "isGray";

	public static final String IS_GRAY_DEFAULT_FLAG = "false";

	private static final Logger logger = LoggerFactory.getLogger(GrayMetadataRule.class);

	@Override
	public Server choose(Object key) {

		DynamicServerListLoadBalancer iLoadBalancer = (DynamicServerListLoadBalancer) super.getLoadBalancer();
		String serverId = iLoadBalancer.getName();
		System.out.println(serverId);
		List<Server> serverList = this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers(), key);
		if (serverList==null || serverList.size() ==0) {
			return null;
		}

		String version  = ThreadLocalContext.version.get();
		if(version == null ) return  originChoose(serverList,key);
		logger.debug("======>GrayMetadataRule:  version{}", version);

		List<Server> noMetaServerList = new ArrayList<>();
		for (Server server : serverList) {
			Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();




			String metaVersion = metadata.get(META_DATA_KEY_VERSION);
			if (!StringUtils.isEmpty(metaVersion)) {
				if (metaVersion.equals(version)) {
					return server;
				}
			}

			if(!Boolean.valueOf(metadata.getOrDefault(IS_GRAY_FLAG,IS_GRAY_DEFAULT_FLAG))){
				noMetaServerList.add(server);
			}


		}


		return originChoose(noMetaServerList, key);
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