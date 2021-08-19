package com.wanmi.sbc.setting.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统配置实体类
 * Created by dyt on 2017/4/23.
 */
@Entity
@Table(name = "system_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Config implements Serializable {

    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 配置键
     */
    @Column(name = "config_key")
    private String configKey;

    /**
     * 类型
     */
    @Column(name = "config_type")
    private String configType;

    /**
     * 名称
     */
    @Column(name = "config_name")
    private String configName;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 内容
     */
    @Column(name = "context")
    private String context;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    @NotNull
    private DeleteFlag delFlag;
}
