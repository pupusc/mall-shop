package com.wanmi.sbc.customer.detail.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户详细信息
 * Created by CHENLI on 2017/4/13.
 */
@Data
@NoArgsConstructor
public class CustomerDetailInitEs implements Serializable {

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 省
     */
    private Long provinceId;

    /**
     * 市
     */
    private Long cityId;

    /**
     * 区
     */
    private Long areaId;

    /**
     * 街道
     */
    private Long streetId;

    /**
     * 详细地址
     */
    private String customerAddress;

    /**
     * 账号状态 0：启用中  1：禁用中
     */
    private CustomerStatus customerStatus;

    /**
     * 联系人名字
     */
    private String contactName;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 负责业务员
     */
    private String employeeId;


    /**
     * 是否为分销员 0：否  1：是
     */
    private DefaultFlag isDistributor;

    /**
     * 审核驳回理由
     */
    private String rejectReason;

    /**
     * 禁用原因
     */
    private String forbidReason;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;


    public CustomerDetailInitEs(String customerId, String customerName, Long provinceId, Long cityId, Long areaId, Long streetId,
                                String customerAddress, CustomerStatus customerStatus, String contactName, String contactPhone,
                                String employeeId, DefaultFlag isDistributor, String rejectReason, String forbidReason, LocalDateTime createTime) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.streetId = streetId;
        this.customerAddress = customerAddress;
        this.customerStatus = customerStatus;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.employeeId = employeeId;
        this.isDistributor = isDistributor;
        this.rejectReason = rejectReason;
        this.forbidReason = forbidReason;
        this.createTime = createTime;
    }
}
