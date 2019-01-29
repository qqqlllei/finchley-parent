package com.gray.core.util;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.springframework.util.StringUtils;

/**
 * Created by 李雷 on 2019/1/24.
 */
public class ThreadLocalContext {

    public static final HystrixRequestVariableDefault<String> version = new HystrixRequestVariableDefault<>();

    public static void initHystrixRequestContext(String headerVer) {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }

        if (!StringUtils.isEmpty(headerVer)) {
            ThreadLocalContext.version.set(headerVer);
        } else {
            ThreadLocalContext.version.set("");
        }
    }

    public static void shutdownHystrixRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }
}
