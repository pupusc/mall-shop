package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物流信息
 * @author wumeng[OF2627]
 *         company qianmi.com
 *         Date 2017-04-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class LogisticsDTO implements Serializable {

    /**
     * 物流配送方式编号
     */
    @ApiModelProperty(value = "物流配送方式编号")
    private String shipMethodId;

    /**
     * 物流配送方式名称
     */
    @ApiModelProperty(value = "物流配送方式名称")
    private String shipMethodName;

    /**
     * 物流号
     */
    @ApiModelProperty(value = "物流号")
    private String logisticNo;

    /**
     * 物流费
     */
    @ApiModelProperty(value = "物流费")
    private BigDecimal logisticFee;

    /**
     * 物流公司编号
     */
    @ApiModelProperty(value = "物流公司编号")
    private String logisticCompanyId;

    /**
     * 物流公司名称
     */
    @ApiModelProperty(value = "物流公司名称")
    private String logisticCompanyName;

    /**
     * 物流公司标准编码
     */
    @ApiModelProperty(value = "物流公司标准编码")
    private String logisticStandardCode;

    /**
     * 第三方平台物流对应的订单id
     */
    @ApiModelProperty(value = "第三方平台物流对应的订单id")
    private String thirdPlatformOrderId;

    /**
     * 第三方平台外部订单id
     * linkedmall --> 淘宝订单号
     */
    @ApiModelProperty(value = "第三方平台外部订单id，linkedmall --> 淘宝订单号")
    private String outOrderId;

    /**
     * 购买用户id
     */
    @ApiModelProperty(value = "购买用户id")
    private String buyerId;

}
