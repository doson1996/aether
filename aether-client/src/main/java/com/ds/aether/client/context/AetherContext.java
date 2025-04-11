package com.ds.aether.client.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ds.aether.client.job.JobInfo;

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

}
