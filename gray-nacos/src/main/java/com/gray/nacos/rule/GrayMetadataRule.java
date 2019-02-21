package com.gray.nacos.rule;

import com.gray.nacos.predicate.NacosServerPredicate;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import com.netflix.loadbalancer.Server;

public class GrayMetadataRule extends PredicateBasedRule {

	private CompositePredicate compositePredicate;

	private NacosServerPredicate nacosServerPredicate;

	public GrayMetadataRule(){
		super();
		nacosServerPredicate= new NacosServerPredicate(this,null);
		compositePredicate = CompositePredicate.withPredicate(nacosServerPredicate).build();
	}

	@Override
	public Server choose(Object key) {
		return super.choose(key);
	}




	@Override
	public AbstractServerPredicate getPredicate() {
		return compositePredicate;
	}

	public NacosServerPredicate getNacosServerPredicate() {
		return nacosServerPredicate;
	}
}