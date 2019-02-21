package com.gray.nacos.util;

import java.util.Map;

/**
 * Created by 李雷 on 2019/1/24.
 */
public class ThreadLocalContext {

    private static ThreadLocal<Map<String,String>> threadLocal = new ThreadLocal<>();

    public static void set(Map<String,String> threadLocalItem){

        threadLocal.set(threadLocalItem);
    }

    public static Map<String,String> get(){
        return threadLocal.get();
    }

    public static void clear(){
        threadLocal.remove();
    }
}
