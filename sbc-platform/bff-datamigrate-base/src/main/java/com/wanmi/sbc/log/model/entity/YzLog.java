package com.wanmi.sbc.log.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by hht on 2017/12/4.
 */
@Data
@Builder
@Entity
@Table(name = "yz_log")
public class YzLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 模块("order","customer","goods")
     */
    @Column(name = "req_mod")
    private String mod;

    /**
     * 条件
     */
    @Column(name = "req_condition")
    private String condition;

    /**
     * 异常信息
     */
    @Column(name = "req_exception")
    private String exception;

    /**
     * 创建时间
     */
    @Column(name = "req_create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;


}
