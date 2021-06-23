package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@ApiModel
public class PurchaseDTO implements Serializable {

    private static final long serialVersionUID = 4916802431164179742L;

    /**
     * 采购单编号
     */
    @ApiModelProperty(value = "采购单编号")
    private Long purchaseId;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * 全局购买数
     */
    @ApiModelProperty(value = "全局购买数")
    private Long goodsNum;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 采购创建时间
     */
    @ApiModelProperty(value = "采购创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 分销员Id
     */
    @ApiModelProperty(value = "分销员Id")
    private String inviteeId;

}
