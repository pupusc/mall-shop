package com.wanmi.sbc.elastic.customer.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.elastic.bean.constant.customer.CustomerConstant;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = EsConstants.DOC_CUSTOMER_DETAIL, type = EsConstants.DOC_CUSTOMER_DETAIL)
@Data
public class EsCustomerDetail implements Serializable {

    /**
     * 会员ID
     */
    @Id
    private String customerId;

    /**
     * 会员名称
     */
    @Field(type = FieldType.Keyword)
    private String customerName;

    /**
     * 账户
     */
    @Field(type = FieldType.Keyword)
    private String customerAccount;

    /**
     * 省
     */
    @Field(type = FieldType.Long)
    private Long provinceId;

    /**
     * 市
     */
    @Field(type = FieldType.Long)
    private Long cityId;

    /**
     * 区
     */
    @Field(type = FieldType.Long)
    private Long areaId;

    /**
     * 街道
     */
    @Field(type = FieldType.Long)
    private Long streetId;

    /**
     * 详细地址
     */
    @Field(type = FieldType.Text,index = false)
    private String customerAddress;

    /**
     * 联系人名字
     */
    @Field(type = FieldType.Keyword)
    private String contactName;

    /**
     * 客户等级ID
     */
    @Field(type = FieldType.Long)
    private Long customerLevelId;


    /**
     * 联系方式
     */
    @Field(type = FieldType.Keyword)
    private String contactPhone;


    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @Field(type = FieldType.Integer)
    private CheckState checkState;


    /**
     * 账号状态 0：启用中  1：禁用中
     */
    @Field(type = FieldType.Integer)
    private CustomerStatus customerStatus;


    /**
     * 负责业务员
     */
    @Field(type = FieldType.Keyword)
    private String employeeId;


    /**
     * 是否为分销员 0：否  1：是
     */
    @Field(type = FieldType.Integer)
    private DefaultFlag isDistributor;


    /**
     * 审核驳回理由
     */
    @Field(type = FieldType.Text,index = false)
    private String rejectReason;

    /**
     * 禁用原因
     */
    @Field(type = FieldType.Text,index = false)
    private String forbidReason;

    /**
     * 店铺等级标识
     */
    @Field(type = FieldType.Nested)
    private List<EsStoreCustomerRela> esStoreCustomerRelaList = new ArrayList<>();

    @Field(type = FieldType.Nested)
    private EsEnterpriseInfo enterpriseInfo;

    /**
     * 企业购会员审核状态  0：无状态 1：待审核 2：已审核 3：审核未通过
     */
    @Field(type = FieldType.Integer)
    private EnterpriseCheckState enterpriseCheckState;

    @Field(type = FieldType.Nested)
    private List<EsPaidCard> esPaidCardList = new ArrayList<>();

    /**
     * 企业购会员审核拒绝原因
     */
    @Field(type = FieldType.Text,index = false)
    private String enterpriseCheckReason;

    @Transient
    private Integer isPaidCardCustomer = 0;
    /**
     * 创建时间
     */
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Date)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    public List<EsStoreCustomerRela> getEsStoreCustomerRelaList() {
        return CollectionUtils.isEmpty(esStoreCustomerRelaList) ? Lists.newArrayList() : esStoreCustomerRelaList;
    }


}
