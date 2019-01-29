package com.server.a.Fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 李雷 on 2019/1/24.
 */
@FeignClient("demo-b")
public interface BServerApi {

    @RequestMapping(value="/demo/info")
    void info();
}
