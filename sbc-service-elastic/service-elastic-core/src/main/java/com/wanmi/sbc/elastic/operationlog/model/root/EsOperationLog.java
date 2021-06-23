package com.wanmi.sbc.elastic.operationlog.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * @author houshuai
 */
@Data
@Document(indexName = EsConstants.SYSTEM_OPERATION_LOG, type = EsConstants.SYSTEM_OPERATION_LOG)
public class EsOperationLog {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 员工编号
     */
    @Field(type = FieldType.Keyword)
    private String employeeId;

    /**
     * 门店Id
     */
    @Field(type = FieldType.Keyword)
    private Long storeId;

    /**
     * 公司信息Id
     */
    @Field(type = FieldType.Keyword)
    private Long companyInfoId;

    /**
     * 操作人账号
     */
    @Field(type = FieldType.Keyword)
    private String opAccount;

    /**
     * 操作人名称
     */
    @Field(type = FieldType.Keyword)
    private String opName;

    /**
     * 操作人角色
     */
    @Field(type = FieldType.Keyword)
    private String opRoleName;

    /**
     * 操作模块
     */
    @Field(type = FieldType.Keyword)
    private String opModule;

    /**
     * 操作类型
     */
    @Field(type = FieldType.Keyword)
    private String opCode;

    /**
     * 操作内容
     */
    @Field(type = FieldType.Keyword)
    private String opContext;

    /**
     * 操作时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime opTime;

    /**
     * 操作IP
     */
    @Field(type = FieldType.Keyword)
    private String opIp;

    /**
     * 操作MAC地址
     */
    @Field(type = FieldType.Keyword)
    private String opMac;

    /**
     * 运营商
     */
    @Field(type = FieldType.Keyword)
    private String opIsp;

    /**
     * 所在国家
     */
    @Field(type = FieldType.Keyword)
    private String opCountry;

    /**
     * 所在省份
     */
    @Field(type = FieldType.Keyword)
    private String opProvince;

    /**
     * 所在城市
     */
    @Field(type = FieldType.Keyword)
    private String opCity;
}