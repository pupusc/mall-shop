package com.fangdeng.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTradeDTO implements Serializable {


    private String platformCode;
    /**
     * 物流费用
     */
    @ApiModelProperty(value = "物流费用")
    private BigDecimal postFee;


    @ApiModelProperty(value = "买家留言")
    private String buyerMemo;


    /**
     *商品明细
     */
    @ApiModelProperty(value = "商品明细")
    private List<TradeItemDTO> details;


    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String receiverName;

    /**
     *固定电话
     */
    @ApiModelProperty(value = "固定电话")
    private String receiverPhone;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String receiverMobile;

    /**
     * 邮政编码
     */
    @ApiModelProperty(value = "邮政编码")
    private String receiverZip;

    /**
     * 省名称
     */
    @ApiModelProperty(value = "省名称")
    private String receiverProvince;

    /**
     * 市名称
     */
    @ApiModelProperty(value = "市名称")
    private String receiverCity;

    /**
     * 区名称
     */
    @ApiModelProperty(value = "区名称")
    private String receiverDistrict;

    /**
     *收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String receiverAddress;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String vipRealName;

    /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "电子邮箱")
    private String vipEmail;

}

