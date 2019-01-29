package com.gateway.server.util;

import com.gateway.server.model.GatewayPredicateDefinition;
import com.gateway.server.model.GatewayRouteDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李雷 on 2019/1/14.
 */
public class GatewayUtil {


    public static RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        List<PredicateDefinition> pdList=new ArrayList<>();
        definition.setId(gwdefinition.getId());
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList=gwdefinition.getPredicates();
        for (GatewayPredicateDefinition gpDefinition: gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);


//        URI uri = UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri()).build().toUri();
        definition.setUri( getServerUri(gwdefinition.getUri()));
        return definition;
    }

    public static URI getServerUri(String serviceUri){
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression urlExpr = parser.parseExpression(serviceUri);
        String uri  = urlExpr.getValue(String.class);
        return URI.create(uri);
    }
}
