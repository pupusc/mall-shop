package com.wanmi.sbc.setting.email;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.EmailStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮箱服务器设置
 */
@Data
@Entity
@Table(name = "system_email_config")
public class EmailConfig implements Serializable {

    /**
     * 邮箱配置Id，采用UUID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "email_config_id")
    private String emailConfigId;

    /**
     * 发信人邮箱地址
     */
    @Column(name = "from_email_address")
    private String fromEmailAddress;

    /**
     * 发信人
     */
    @Column(name = "from_person")
    private String fromPerson;

    /**
     * SMTP服务器主机名
     */
    @Column(name = "email_smtp_host")
    private String emailSmtpHost;

    /**
     * SMTP服务器端口号
     */
    @Column(name = "email_smtp_port")
    private String emailSmtpPort;

    /**
     * SMTP服务器授权码
     */
    @Column(name = "auth_code")
    private String authCode;

    /**
     * 邮箱启用状态（0：未启用 1：已启用）
     */
    private EmailStatus status;

    /**
     * 删除标记  0未删除  1已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

}
