package com.gray.gateway.predicate;

import com.gray.gateway.service.NacosConfigToCacheService;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidancePredicate;
import org.springframework.lang.Nullable;

/**
 * Created by 李雷 on 2019/2/20.
 */
public class NacosServerPredicate extends ZoneAvoidancePredicate {

    private NacosConfigToCacheService nacosConfigToCacheService;

    public NacosServerPredicate(IRule rule, IClientConfig clientConfig) {
        super(rule, clientConfig);
    }

    public void setNacosConfigToCacheService(NacosConfigToCacheService nacosConfigToCacheService) {
        this.nacosConfigToCacheService = nacosConfigToCacheService;
    }

    @Override
    public boolean apply(@Nullable PredicateKey input) {
        boolean enabled = super.apply(input);
        if (!enabled) {
            return false;
        }
        return apply(input.getServer());
    }

    private boolean apply(Server server){
       if(nacosConfigToCacheService.filterVersion(server) && nacosConfigToCacheService.filterIp(server)) {
           return true;
       }
        return false;
    }

}