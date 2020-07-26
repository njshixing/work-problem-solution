package cn.raulism.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.pattern.CronPatternUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * 这个方案实现有问题
 */
@Lazy(value = false)
@Component
@Slf4j
@Data
@EnableScheduling
public class ScheduleTask implements SchedulingConfigurer {
    public static Map<String, ScheduledFuture<?>> scheduledFutureMap = Maps.newConcurrentMap();
    public static Map<String, ThreadPoolTaskScheduler> stringScheduledExecutorServiceMap = Maps.newConcurrentMap();
//    private static ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//
//    static {
//        threadPoolTaskScheduler.setPoolSize(20);
//        threadPoolTaskScheduler.setThreadNamePrefix("Scheduled-");
//        threadPoolTaskScheduler.setRejectedExecutionHandler((r, e) -> {
//            if (!e.isShutdown()) {
//                r.run();
//            }
//        });
//        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
//        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
//        threadPoolTaskScheduler.initialize();
//    }

    private String cron;
    private String planCode;

    public static void cancel(ScheduleTask task) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.remove(task.getPlanCode());
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(Boolean.TRUE);
            stringScheduledExecutorServiceMap.get(task.getPlanCode()).shutdown();
        }
        log.info("取消定时任务" + task.getPlanCode());
    }

    public static void reset(ScheduleTask task) {
        log.info("修改定时任务开始" + task.getPlanCode());
        cancel(task);
        start(task);
        log.info("修改定时任务结束" + task.getPlanCode());
    }

    public static void start(ScheduleTask task) {
//        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(getRunnable(task), getTrigger(task));
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        ScheduledFuture<?> scheduledFuture = scheduler.schedule(getRunnable(task), getTrigger(task));
        scheduledFutureMap.put(task.getPlanCode(), scheduledFuture);
        stringScheduledExecutorServiceMap.put(task.getPlanCode(), scheduler);
        log.info("启动定时任务" + task.getPlanCode());
    }

    private static Runnable getRunnable(ScheduleTask task) {
        return new Runnable() {
            @Override
            public void run() {
                log.info("本次执行 {} {}", task.getPlanCode(), DateUtil.formatDateTime(new Date()));
            }
        };
    }

    private static Trigger getTrigger(ScheduleTask task) {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                Date date = CronPatternUtil.nextDateAfter(new CronPattern(task.getCron()), DateUtil.offsetSecond(new Date(), 1).toJdkDate(), true);
                log.info("下次执行 {} {}", task.getPlanCode(), DateUtil.formatDateTime(date));
                return date;
            }
        };
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<ScheduleTask> tasks = getAllScheduleTasks();
        log.info("【定时任务启动】 启动任务数：{}", tasks.size());

        // 通过校验的数据执行定时任务
        if (tasks.size() > 0) {
            for (ScheduleTask task : tasks) {
                try {
                    start(task);
                } catch (Exception e) {
                    log.error("task start error:", e);
                }
            }
        }
    }

    private List<ScheduleTask> getAllScheduleTasks() {
        List<ScheduleTask> list = Lists.newArrayList();
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setCron("0/10 * * * * *");
        scheduleTask.setPlanCode("1");
        ScheduleTask scheduleTask2 = new ScheduleTask();
        scheduleTask2.setCron("0/15 * * * * *");
        scheduleTask2.setPlanCode("2");
        list.add(scheduleTask);
        list.add(scheduleTask2);
        return list;
    }


}
