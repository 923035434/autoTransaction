package com.ll.autotransaction.util;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalCacheUtil {

    // 定时任务执行器
    private static ScheduledExecutorService refreshExecutor = new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory());

    // 当前缓存
    private static volatile ConcurrentHashMap<String, CacheElement<Object>> cacheMap = new ConcurrentHashMap<>();

    // 缓存对象
    static class CacheElement<T extends Object> {

        // 缓存值
        private T value;

        // 过期时间
        private Long expire;

        /**
         * 有参数构造方法
         *
         * @param value 缓存的值
         * @param timeout 过期时间。单位：ms
         */
        public CacheElement(T value, Long timeout) {
            this.value = value;
            this.expire = System.currentTimeMillis() + timeout;
        }

        /**
         * 判断缓存是否过期
         */
        public boolean isOvertime() {
            return expire < System.currentTimeMillis();
        }
    }

    /**
     * 读取本地缓存
     *
     * @param key 缓存key
     * @param requireClass 缓存值对应class
     */
    public static <T extends Object> T load(String key, Class<T> requireClass) {
        CacheElement<Object> e = cacheMap.get(key);
        //过期返回null
        if (null == e || e.isOvertime()) {
            return null;
        }
        return TypeUtils.cast(e.value, requireClass, ParserConfig.getGlobalInstance());
    }

    /**
     * 保存至本地缓存
     *
     * @param key 缓存key
     * @param value 缓存值
     * @param timeout 过期时间。单位：ms
     */
    public static <T extends Object> void save(String key, T value, Long timeout) {
        cacheMap.put(key, new CacheElement<>(value, timeout));
    }

    /**
     * 手动清除key对应的本地缓存
     */
    public static void clearCache(String key) {
        cacheMap.put(key, null);
    }

    /**
     * 定时（每5分钟）清理一次缓存中过期的数据
     */
    static {
        refreshExecutor.scheduleAtFixedRate(() -> {
            ConcurrentHashMap<String, CacheElement<Object>> newCache = new ConcurrentHashMap<>();
            for (Map.Entry<String, CacheElement<Object>> e : cacheMap.entrySet()) {
                // 丢弃已经过期的数据
                if (e.getValue().isOvertime()) {
                    continue;
                }
                newCache.put(e.getKey(), e.getValue());
            }
            cacheMap = newCache;
        }, 0, 60, TimeUnit.SECONDS);
    }

}
