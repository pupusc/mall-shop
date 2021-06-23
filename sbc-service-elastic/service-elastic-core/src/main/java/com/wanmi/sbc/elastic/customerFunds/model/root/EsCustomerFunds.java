package com.wanmi.sbc.elastic.customerFunds.model.root;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName EsCustomerFunds
 * @Description es 会员资金
 * @Author yangzhen
 * @Date 2020/12/15 10:29
 * @Version 1.0
 */
@Document(indexName = EsConstants.DOC_CUSTOMER_FUNDS, type = EsConstants.DOC_CUSTOMER_FUNDS)
@Data
public class EsCustomerFunds {


    @Id
    private String id;

    /**
     * 主键
     */
    @Field(type = FieldType.Text)
    private String customerFundsId;

    /**
     * 会员ID
     */
    @Field(type = FieldType.Text)
    private String customerId;

    /**
     * 会员登录账号|手机号
     */
    @Field(type = FieldType.Keyword)
    private String customerAccount;

    /**
     * 会员名称
     */
    @Field(type = FieldType.Keyword)
    private String customerName;

    /**
     * 账户余额
     */
    @Field(type = FieldType.Double)
    private BigDecimal accountBalance;

    /**
     * 冻结余额
     */
    @Field(type = FieldType.Double)
    private BigDecimal blockedBalance;

    /**
     * 可提现金额
     */
    @Field(type = FieldType.Double)
    private BigDecimal withdrawAmount;



    /**
     * 是否分销员 0：否，1：是
     */
    @Field(type = FieldType.Integer)
    private Integer distributor;


    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;



}
