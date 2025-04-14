package com.ds.aether.server.storage;

import com.ds.aether.core.model.server.ExecutorInfo;

import java.util.Map;

/**
 * @author ds
 * @date 2025/4/13
 * @description 执行器存储接口
 */
public interface ExecutorStorage {

    /**
     * 查询所有可用的执行器信息
     * @return
     */
    Map<String, ExecutorInfo> findAvailableExecutors();

    /**
     * 查询所有执行器信息
     *
     * @return
     */
    Map<String, ExecutorInfo> findAll();

    /**
     * 查询执行器信息
     *
     * @param name 执行器名
     * @return
     */
    ExecutorInfo find(String name);

    /**
     * 执行器是否存在
     *
     * @param name 执行器名
     * @return
     */
    boolean exist(String name);

    /**
     * 添加执行器信息
     *
     * @param executorInfo 执行器信息
     * @return
     */
    boolean add(ExecutorInfo executorInfo);

    /**
     * 移除执行器信息
     *
     * @param name 执行器名
     * @return
     */
    boolean remove(String name);

    /**
     * 移除所有执行器信息
     */
    void removeAll();

    /**
     * 更新执行器信息
     *
     * @param executorInfo 执行器信息
     */
    void update(ExecutorInfo executorInfo);

}