package com.ds.aether.server.util;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/8/26
 * @description 本地缓存
 */
@Slf4j
public class LocalCache {

    private static final Cache<String, Object> CACHE;

    static {
        // 创建带过期策略的缓存
        CACHE = CacheBuilder.newBuilder()
                // 设置缓存最大条目数
                .maximumSize(100)
                // 设置写入后10分钟过期
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 设置访问后5分钟过期（每次访问会重置过期时间）
                .expireAfterAccess(5, TimeUnit.MINUTES)
                // 设置缓存移除监听器
                .removalListener((RemovalListener<String, Object>) notification -> log.info("缓存项被移除 - Key: {}, Value: {}, 原因: {}",
                        notification.getKey(),
                        notification.getValue(),
                        notification.getCause()))
                // 开启统计信息
                .recordStats()
                .build();
    }

    /**
     * 放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, Object value) {
        CACHE.put(key, value);
    }

    /**
     * 获取缓存值
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return CACHE.getIfPresent(key);
    }

    /**
     * 获取缓存值，如果不存在则通过Callable加载
     *
     * @param key      键
     * @param callable 数据加载器
     * @return 值
     */
    public static Object get(String key, Callable<Object> callable) {
        try {
            return CACHE.get(key, callable);
        } catch (Exception e) {
            log.error("获取缓存异常：", e);
        }
        return null;
    }

    /**
     * 删除缓存项
     *
     * @param key 键
     */
    public static void remove(String key) {
        CACHE.invalidate(key);
    }

    /**
     * 批量删除缓存项
     *
     * @param keys 键列表
     */
    public static void removeAll(Iterable<String> keys) {
        CACHE.invalidateAll(keys);
    }

    /**
     * 清空所有缓存
     */
    public static void clearAll() {
        CACHE.invalidateAll();
    }

    /**
     * 获取缓存统计信息
     *
     * @return 统计信息字符串
     */
    public static String getStats() {
        return CACHE.stats().toString();
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存大小
     */
    public static long size() {
        return CACHE.size();
    }

}
