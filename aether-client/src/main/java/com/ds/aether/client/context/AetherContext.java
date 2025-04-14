package com.ds.aether.client.context;

import com.ds.aether.core.job.JobInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ds
 * @date 2025/4/11
 * @description aether上下文
 */
public class AetherContext {

    private static final Map<String, JobInfo> JOB_INFO_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 添加任务信息
     *
     * @param jobName
     * @param jobInfo
     */
    public static void addJobInfo(String jobName, JobInfo jobInfo) {
        JOB_INFO_REPOSITORY.put(jobName, jobInfo);
    }

    /**
     * 获取任务信息
     *
     * @param jobName
     */
    public static JobInfo getJobInfo(String jobName) {
        return JOB_INFO_REPOSITORY.get(jobName);
    }

    /**
     * 是否已经任务信息
     *
     * @param jobName
     */
    public static boolean containsJobInfo(String jobName) {
        return JOB_INFO_REPOSITORY.containsKey(jobName);
    }

    /**
     * 获取所有任务信息
     */
    public static Map<String, JobInfo> allJobInfo() {
        return JOB_INFO_REPOSITORY;
    }

}
