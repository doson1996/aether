package com.ds.aether.server.scheduler;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ds.aether.core.context.SpringContext;
import com.ds.aether.server.job.ExecJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * @author ds
 * @date 2025/8/13
 * @description 分布式任务调度器
 */
@Slf4j
public class DistributedCronScheduler implements Scheduler {

    private final RedisTemplate<String, String> redisTemplate;

    private final ScheduledExecutorService scheduler;

    private final ScheduledExecutorService coordinationScheduler;

    private final String nodeId;

    private final AtomicBoolean running = new AtomicBoolean(false);

    // Redis中存储任务信息的前缀
    private static final String JOB_INFO_PREFIX = "aether:job:";
    // Redis中存储任务锁的键
    private static final String JOB_LOCK_PREFIX = "aether:lock:";
    // Redis中存储活跃节点的键
    private static final String NODE_PREFIX = "aether:node:";

    public DistributedCronScheduler(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.scheduler = Executors.newScheduledThreadPool(10);
        this.coordinationScheduler = Executors.newSingleThreadScheduledExecutor();
        this.nodeId = UUID.randomUUID().toString();
    }

    /**
     * 注册并调度任务
     */
    @Override
    public void schedule(String cronExpression, String jobName) {
        if (!running.get()) {
            throw new IllegalStateException("调度器未启动");
        }

        // 在Redis中注册任务信息
        registerJob(jobName, cronExpression);
    }

    /**
     * 取消任务调度
     */
    @Override
    public boolean cancel(String jobName) {
        String jobInfoKey = JOB_INFO_PREFIX + jobName;
        Boolean deleted = redisTemplate.delete(jobInfoKey);
        // 同时删除锁
        String lockKey = JOB_LOCK_PREFIX + jobName;
        redisTemplate.delete(lockKey);
        return Boolean.TRUE.equals(deleted);
    }

    @Override
    public boolean isScheduled(String jobName) {
        String jobInfoKey = JOB_INFO_PREFIX + jobName;
        Object cronObj = redisTemplate.opsForHash().get(jobInfoKey, "cron");
        return cronObj != null && !cronObj.toString().isEmpty();
    }

    @Override
    public Long getScheduledTaskCount() {
        Set<String> jobKeys = redisTemplate.keys(JOB_INFO_PREFIX + "*");
        if (jobKeys.isEmpty()) {
            return 0L;
        }

        long count = 0;
        for (String jobKey : jobKeys) {
            try {
                // 检查任务是否包含必要的调度信息
                Object cronObj = redisTemplate.opsForHash().get(jobKey, "cron");
                if (cronObj != null && !cronObj.toString().isEmpty()) {
                    count++;
                }
            } catch (Exception e) {
                // 如果出现类型错误，跳过该键
                log.debug("检查任务时出错: {}", jobKey, e);
            }
        }
        return count;
    }

    /**
     * 启动调度器
     */
    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            // 注册当前节点
            registerNode();
            // 启动协调任务
            coordinationScheduler.scheduleAtFixedRate(
                    this::coordinateJobs, 0, 1, TimeUnit.SECONDS);
            log.info("分布式调度器已启动，节点ID: {}", nodeId);
        }
    }

    /**
     * 停止调度器
     */
    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            // 取消协调任务
            coordinationScheduler.shutdown();

            // 注销当前节点
            unregisterNode();

            // 关闭执行线程池
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }

            log.info("分布式调度器已停止");
        }
    }

    /**
     * 在Redis中注册任务
     */
    private void registerJob(String jobName, String cronExpression) {
        String jobInfoKey = JOB_INFO_PREFIX + jobName;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecution = calculateNextExecution(now, cronExpression);

        redisTemplate.opsForHash().put(jobInfoKey, "cron", cronExpression);
        redisTemplate.opsForHash().put(jobInfoKey, "created", now.toString());
        redisTemplate.opsForHash().put(jobInfoKey, "next_execution",
                nextExecution != null ? nextExecution.toString() : "");
        redisTemplate.opsForHash().put(jobInfoKey, "last_updated", nodeId);
    }

    /**
     * 注册当前节点
     */
    private void registerNode() {
        String nodeKey = NODE_PREFIX + nodeId;
        redisTemplate.opsForValue().set(nodeKey, LocalDateTime.now().toString(),
                30, TimeUnit.SECONDS); // 设置30秒过期时间
        // 定期更新节点状态
        scheduler.scheduleWithFixedDelay(() -> {
            if (running.get()) {
                redisTemplate.opsForValue().set(nodeKey, LocalDateTime.now().toString(),
                        30, TimeUnit.SECONDS);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 注销当前节点
     */
    private void unregisterNode() {
        String nodeKey = NODE_PREFIX + nodeId;
        redisTemplate.delete(nodeKey);
    }

    /**
     * 协调任务执行
     */
    private void coordinateJobs() {
        if (!running.get()) {
            return;
        }

        try {
            // 获取所有注册的任务
            redisTemplate.keys(JOB_INFO_PREFIX + "*").forEach(jobKey -> {
                String jobName = jobKey.substring(JOB_INFO_PREFIX.length());
                try {
                    if (shouldExecute(jobName) && acquireLock(jobName)) {
                        // 异步执行任务
                        scheduler.submit(() -> {
                            try {
                                executeJob(jobName);
                            } catch (Exception e) {
                                log.error("执行任务失败: {}", jobName, e);
                            } finally {
                                releaseLock(jobName);
                                updateNextExecutionTime(jobName);
                            }
                        });
                    }
                } catch (Exception e) {
                    log.error("协调任务执行出错: {}", jobName, e);
                }
            });
        } catch (Exception e) {
            log.error("协调任务出错", e);
        }
    }

    /**
     * 判断任务是否应该执行
     */
    private boolean shouldExecute(String jobName) {
        String jobInfoKey = JOB_INFO_PREFIX + jobName;
        Object nextExecutionObj = redisTemplate.opsForHash().get(jobInfoKey, "next_execution");

        if (nextExecutionObj == null || nextExecutionObj.toString().isEmpty()) {
            return false;
        }

        try {
            LocalDateTime nextExecution = LocalDateTime.parse(nextExecutionObj.toString());
            return LocalDateTime.now().compareTo(nextExecution) >= 0;
        } catch (Exception e) {
            log.warn("解析任务下次执行时间失败: {}", jobName, e);
            return false;
        }
    }

    /**
     * 获取任务执行锁
     */
    private boolean acquireLock(String jobName) {
        String lockKey = JOB_LOCK_PREFIX + jobName;
        String lockScript =
                "local lock = redis.call('GET', KEYS[1]) " +
                        "if not lock or lock == ARGV[1] then " +
                        "  redis.call('SET', KEYS[1], ARGV[1]) " +
                        "  redis.call('EXPIRE', KEYS[1], ARGV[2]) " +
                        "  return 1 " +
                        "else " +
                        "  return 0 " +
                        "end";

        RedisScript<Long> script = new DefaultRedisScript<>(lockScript, Long.class);
        Long result = redisTemplate.execute(script,
                java.util.Collections.singletonList(lockKey),
                nodeId, "60"); // 60秒锁超时

        return Long.valueOf(1).equals(result);
    }

    /**
     * 释放任务执行锁
     */
    private void releaseLock(String jobName) {
        String lockKey = JOB_LOCK_PREFIX + jobName;
        String releaseScript =
                "if redis.call('GET', KEYS[1]) == ARGV[1] then " +
                        "  return redis.call('DEL', KEYS[1]) " +
                        "else " +
                        "  return 0 " +
                        "end";

        RedisScript<Long> script = new DefaultRedisScript<>(releaseScript, Long.class);
        redisTemplate.execute(script,
                java.util.Collections.singletonList(lockKey),
                nodeId);
    }

    /**
     * 执行任务
     */
    private void executeJob(String jobName) {
        try {
            log.debug("执行分布式任务：{}", jobName);
            ExecJobHelper execJobHelper = SpringContext.getContext().getBean(ExecJobHelper.class);
            execJobHelper.start(jobName);
            // 更新任务执行状态
            String jobInfoKey = JOB_INFO_PREFIX + jobName;
            redisTemplate.opsForHash().put(jobInfoKey, "last_execution", LocalDateTime.now().toString());
        } catch (Exception e) {
            log.error("执行分布式任务失败: {}", jobName, e);
        }
    }

    /**
     * 更新下次执行时间
     */
    private void updateNextExecutionTime(String jobName) {
        try {
            String jobInfoKey = JOB_INFO_PREFIX + jobName;
            Object cronObj = redisTemplate.opsForHash().get(jobInfoKey, "cron");

            if (cronObj != null) {
                LocalDateTime nextExecution = calculateNextExecution(LocalDateTime.now(), cronObj.toString());
                redisTemplate.opsForHash().put(jobInfoKey, "next_execution",
                        nextExecution != null ? nextExecution.toString() : "");
                redisTemplate.opsForHash().put(jobInfoKey, "last_updated", nodeId);
            }
        } catch (Exception e) {
            log.error("更新任务下次执行时间失败: {}", jobName, e);
        }
    }

    /**
     * 计算下次执行时间
     */
    private LocalDateTime calculateNextExecution(LocalDateTime from, String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            LocalDateTime next = from.plusSeconds(1);

            // 查找下一个匹配的时间点
            while (!cron.matches(next)) {
                next = next.plusSeconds(1);

                // 防止无限循环
                if (next.isAfter(from.plusYears(1))) {
                    return null;
                }
            }

            return next;
        } catch (Exception e) {
            log.error("计算下次执行时间失败: {}", cronExpression, e);
            return null;
        }
    }
}
