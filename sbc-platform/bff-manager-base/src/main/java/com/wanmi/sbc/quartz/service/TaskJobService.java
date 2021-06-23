package com.wanmi.sbc.quartz.service;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.quartz.enums.TaskBizType;
import com.wanmi.sbc.quartz.enums.TaskStatus;
import com.wanmi.sbc.quartz.model.entity.TaskInfo;
import com.wanmi.sbc.quartz.repository.TaskJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


/**
 * 定时任务接口
 */
@Slf4j
@Service
public class TaskJobService {

    @Autowired
    private TaskJobRepository taskJobRepository;

    @Autowired
    private QuartzManagerService quartzManagerService;

    public static final String PRECISION_VOUCHERS = "PRECISION_VOUCHERS";

    public static final String MESSAGE_SEND = "MESSAGE_SEND";

    public static final String PRICE_ADJUST = "PRICE_ADJUST";

    /**
     * 新增任务
     * @param taskInfo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskInfo addTaskJob(TaskInfo taskInfo){
        return taskJobRepository.save(taskInfo);
    }

    /**
     * 查询所有自动运行&未结束的任务列表
     * @return
     */
    public List<TaskInfo> findAllAutoRunAndState(){return taskJobRepository.findAllAutoRunAndState();}

    /**
     * 根据业务ID查询任务信息
     * @param bizId
     * @return
     */
    public TaskInfo findByBizId(String bizId){return taskJobRepository.findByBizId(bizId);}

    /**
     * 精准发券的定时任务组装
     * @param activityId 活动ID
     * @param startTime 任务触发时间
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskInfo addOrModifyTaskJob(String activityId, LocalDateTime startTime){
        if (StringUtils.isBlank(activityId) || Objects.isNull(startTime)){
            log.info("精准发券，活动ID/任务触发时间不可为空！");
            return null;
        }
        TaskInfo taskJob = findByBizId(activityId);
        if (Objects.isNull(taskJob)){
            taskJob = new TaskInfo();
        }
        //修改任务时间与旧时间相同，不作更新
        if (Objects.nonNull(taskJob.getId()) && startTime.equals(taskJob.getStartTime())){
            return taskJob;
        }
        //任务时间比当前时间小，延迟5分钟，否则此任务执行不了
        if (startTime.isBefore(LocalDateTime.now())){
            startTime = LocalDateTime.now().plusMinutes(5);
        }
        taskJob.setAutoRun(NumberUtils.INTEGER_ONE);
        taskJob.setBizId(activityId);
        taskJob.setStartTime(startTime);
        taskJob.setBizType(TaskBizType.PRECISION_VOUCHERS);
        taskJob.setJobStartClass("com.wanmi.sbc.quartz.job.PrecisionVouchersJob");
        taskJob.setJobGroup(PRECISION_VOUCHERS);
        taskJob.setJobExpress(DateUtil.format(startTime, "ss mm HH dd MM")+" ? " + startTime.getYear());
        taskJob.setState(TaskStatus.STARTING);
        taskJob.setInfo(activityId+"精准发券定时任务开启");
        String taskId = taskJob.getId();
        taskJob = addTaskJob(taskJob);
        if (Objects.isNull(taskId)){
            quartzManagerService.addJob(taskJob);
        }else{
            quartzManagerService.modifyJob(taskJob);
        }
        return taskJob;
    }

    /**
     * 消息发送的定时任务组装
     * @param messageId 活动ID
     * @param startTime 任务触发时间
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskInfo messageSendTaskJob(String messageId, LocalDateTime startTime){
        if (StringUtils.isBlank(messageId) || Objects.isNull(startTime)){
            log.info("消息发送，消息ID/任务触发时间不可为空！");
            return null;
        }
        TaskInfo taskJob = findByBizId(messageId);
        if (Objects.isNull(taskJob)){
            taskJob = new TaskInfo();
        }
        //修改任务时间与旧时间相同，不作更新
        if (Objects.nonNull(taskJob.getId()) && startTime.equals(taskJob.getStartTime())){
            return taskJob;
        }
        //任务时间比当前时间小，延迟5秒，否则此任务执行不了
        if (startTime.isBefore(LocalDateTime.now())){
            startTime = LocalDateTime.now().plusSeconds(5);
        }
        taskJob.setAutoRun(NumberUtils.INTEGER_ONE);
        taskJob.setBizId(messageId);
        taskJob.setStartTime(startTime);
        taskJob.setBizType(TaskBizType.MESSAGE_SEND);
        taskJob.setJobStartClass("com.wanmi.sbc.quartz.job.MessageSendJob");
        taskJob.setJobGroup(MESSAGE_SEND);
        taskJob.setJobExpress(DateUtil.format(startTime, "ss mm HH dd MM")+" ? " + startTime.getYear());
        taskJob.setState(TaskStatus.STARTING);
        taskJob.setInfo(messageId+"消息发送定时任务开启");
        String taskId = taskJob.getId();
        taskJob = addTaskJob(taskJob);
        if (Objects.isNull(taskId)){
            quartzManagerService.addJob(taskJob);
        }else{
            quartzManagerService.modifyJob(taskJob);
        }
        return taskJob;
    }

    /**
     * 商品调价的定时任务组装
     * @param adjustNo 制单号
     * @param startTime 任务触发时间
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskInfo priceAdjustTaskJob(String adjustNo, LocalDateTime startTime){
        if (StringUtils.isBlank(adjustNo) || Objects.isNull(startTime)){
            log.info("商品调价，制单号/任务触发时间不可为空！");
            return null;
        }
        TaskInfo taskJob = findByBizId(adjustNo);
        if (Objects.nonNull(taskJob)){
           throw new SbcRuntimeException("K-000001");
        } else {
            taskJob = new TaskInfo();
        }
        //任务时间比当前时间小，延迟5秒，否则此任务执行不了
        if (startTime.isBefore(LocalDateTime.now())){
            startTime = LocalDateTime.now().plusSeconds(5);
        }
        taskJob.setAutoRun(NumberUtils.INTEGER_ONE);
        taskJob.setBizId(adjustNo);
        taskJob.setStartTime(startTime);
        taskJob.setBizType(TaskBizType.PRICE_ADJUST);
        taskJob.setJobStartClass("com.wanmi.sbc.quartz.job.PriceAdjustJob");
        taskJob.setJobGroup(PRICE_ADJUST);
        taskJob.setJobExpress(DateUtil.format(startTime, "ss mm HH dd MM")+" ? " + startTime.getYear());
        taskJob.setState(TaskStatus.STARTING);
        taskJob.setInfo(adjustNo+"商品调价定时任务开启");
        String taskId = taskJob.getId();
        taskJob = addTaskJob(taskJob);
        if (Objects.isNull(taskId)){
            quartzManagerService.addJob(taskJob);
        }
        return taskJob;
    }


}
