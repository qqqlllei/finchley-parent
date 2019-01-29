package com.gateway.server.initialization;

import com.gateway.server.model.GatewayRouteDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by 李雷 on 2019/1/14.
 */
@ConfigurationProperties(prefix = "gateway.info")
@Component
public class GatewayInfoProperties {



    private GatewayRouteDefinition[] gatewayRouteDefinitions = {};


    public GatewayRouteDefinition[] getGatewayRouteDefinitions() {
        return gatewayRouteDefinitions;
    }

    public void setGatewayRouteDefinitions(GatewayRouteDefinition[] gatewayRouteDefinitions) {
        this.gatewayRouteDefinitions = gatewayRouteDefinitions;
    }
}
