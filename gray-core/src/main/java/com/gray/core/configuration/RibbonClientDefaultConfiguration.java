package com.gray.core.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * Created by 李雷 on 2019/1/24.
 */
@RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)
public class RibbonClientDefaultConfiguration {
}
