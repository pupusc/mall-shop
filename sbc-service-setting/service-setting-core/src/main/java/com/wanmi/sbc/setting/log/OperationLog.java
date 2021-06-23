package com.wanmi.sbc.setting.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 操作日志信息
 * Created by daiyitian on 2017/4/26.
 */
@Data
@Entity
@Table(name = "system_operation_log")
public class OperationLog {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 员工编号
     */
    @Column(name = "employee_id")
    private String employeeId;

    /**
     * 门店Id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 公司信息Id
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 操作人账号
     */
    @Column(name = "op_account")
    private String opAccount;

    /**
     * 操作人名称
     */
    @Column(name = "op_name")
    private String opName;

    /**
     * 操作人角色
     */
    @Column(name = "op_role_name")
    private String opRoleName;

    /**
     * 操作模块
     */
    @Column(name = "op_module")
    private String opModule;

    /**
     * 操作类型
     */
    @Column(name = "op_code")
    private String opCode;

    /**
     * 操作内容
     */
    @Column(name = "op_context")
    private String opContext;

    /**
     * 操作时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "op_time")
    private LocalDateTime opTime;

    /**
     * 操作IP
     */
    @Column(name = "op_ip")
    private String opIp;

    /**
     * 操作MAC地址
     */
    @Column(name = "op_mac")
    private String opMac;

    /**
     * 运营商
     */
    @Column(name = "op_isp")
    private String opIsp;

    /**
     * 所在国家
     */
    @Column(name = "op_country")
    private String opCountry;

    /**
     * 所在省份
     */
    @Column(name = "op_province")
    private String opProvince;

    /**
     * 所在城市
     */
    @Column(name = "op_city")
    private String opCity;
}
