package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Transient;

/**
 * <p>券码VO</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@Data
public class VirtualCouponCodeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 券码ID
     */
    @ApiModelProperty(value = "券码ID")
    private Long id;

    /**
     * 电子卡券ID
     */
    @ApiModelProperty(value = "电子卡券ID")
    private Long couponId;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Integer validDays;

    /**
     * 0:兑换码 1:券码+密钥 2:链接
     */
    @ApiModelProperty(value = "0:兑换码 1:券码+密钥 2:链接")
    private Integer provideType;

    /**
     * 兑换码/券码/链接
     */
    @ApiModelProperty(value = "兑换码/券码/链接")
    private String couponNo;

    /**
     * 密钥
     */
    @ApiModelProperty(value = "密钥")
    private String couponSecret;

    /**
     * 领取结束时间
     */
    @ApiModelProperty(value = "领取结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime receiveEndTime;

    /**
     * 兑换开始时间
     */
    @ApiModelProperty(value = "兑换开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime exchangeStartTime;

    /**
     * 兑换结束时间
     */
    @ApiModelProperty(value = "兑换结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime exchangeEndTime;

    /**
     * 0:未发放 1:已发放 2:已过期
     */
    @ApiModelProperty(value = "0:未发放 1:已发放 2:已过期")
    private Integer status;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String tid;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updatePerson;

    @Transient
    private LocalDateTime exportTime;

    @Transient
    private String exportStatus;

}