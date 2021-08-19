package com.wanmi.sbc.quartz.service;


import com.wanmi.sbc.quartz.model.entity.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 任务管理类
 */
@Slf4j
@Service
public class QuartzManagerService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 保存定时任务
     * @param info
     */
    public void addJob(TaskInfo info) {
        String bizId = info.getBizId(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getJobExpress(),
                jobDescription = info.getInfo(),
                jobStartClass = info.getJobStartClass(),
                createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            if (checkExists(bizId, jobGroup)) {
                log.info("add job fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, bizId);
                return;
            }
            //创建任务触发器
            CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionIgnoreMisfires();
            TriggerKey triggerKey = TriggerKey.triggerKey(bizId, jobGroup);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(schedBuilder).build();

            //创建任务
            Class<? extends Job> clazz = (Class<? extends Job>)Class.forName(jobStartClass);
            JobKey jobKey = JobKey.jobKey(bizId, jobGroup);
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).usingJobData("bizId",bizId).build();

            //将触发器与任务绑定到调度器内
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException | ClassNotFoundException e) {
            log.error("类名不存在或执行表达式错误,exception:{}",e.getMessage());
        }
    }

    /**
     * 修改定时任务
     * @param info
     */
    public void modifyJob(TaskInfo info) {
        String jobName = info.getBizId(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getJobExpress();
        try {
            if (!checkExists(jobName, jobGroup)) {
                log.info("edit job fail, job is not exist, jobGroup:{}, jobName:{}", jobGroup, jobName);
                return;
            }

            TriggerKey triggerKey =  TriggerKey.triggerKey(jobName, jobGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务,exception:{}",e.getMessage());
        }
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param jobGroup
     */
    public void delete(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
                log.info("delete job, triggerKey:{},jobGroup:{}, jobName:{}", triggerKey ,jobGroup, jobName);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     */
    public void pause(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                log.info("pause job success, triggerKey:{},jobGroup:{}, jobName:{}", triggerKey ,jobGroup, jobName);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 重新开始任务
     */
    public void resume(TaskInfo info){
        String jobName = info.getBizId(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getJobExpress();
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.info("更新定时任务失败"+e);
        }
    }

    /**
     * 验证是否存在
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public boolean checkExists(String jobName, String jobGroup) throws SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }
}
