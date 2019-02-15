package com.gray.gateway.rule;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.*;

import java.util.List;

public class GrayMetadataRule extends PredicateBasedRule {
	private ZoneAvoidancePredicate zoneAvoidancePredicate;

	public GrayMetadataRule(){
		super();
		zoneAvoidancePredicate = new ZoneAvoidancePredicate(this,null);
	}


	@Override
	public Server choose(Object key) {

		ILoadBalancer loadBalancer = getLoadBalancer();
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