package com.wanmi.sbc.quartz.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.quartz.enums.TaskBizType;
import com.wanmi.sbc.quartz.enums.TaskStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务实体类
 * Created by wg on 2017/5/31.
 */
@Data
@Entity
@Table(name = "task_info")
public class TaskInfo implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * 业务类型,描述一类业务范畴
     */
    @Column(name = "biz_type")
    private TaskBizType bizType;

    /**
     * 业务Id
     */
    @Column(name = "biz_id")
    private String bizId;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 定时任务启动类
     */
    @Column(name = "job_start_class")
    private String jobStartClass;

    /**
     * 任务组
     */
    @Column(name = "job_group")
    private String jobGroup;

    /**
     * 任务表达式
     */
    @Column(name = "job_express")
    private String jobExpress;

    /**
     * 是否自动运行
     */
    @Column(name = "auto_run")
    private Integer autoRun;

    /**
     * 定时任务状态
     */
    @Column(name = "state")
    private TaskStatus state;

    /**
     * 描述信息
     */
    @Column(name = "info")
    private String info;
}
