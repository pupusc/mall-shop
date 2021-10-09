package com.fangdeng.server.vo;

import com.fangdeng.server.enums.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeliveryStatusVO implements Serializable {
    /**
     * 发货单对象
     */
    @ApiModelProperty(value = "发货单对象")
    private List<DeliveryInfoVO> deliveryInfoVOList;

    @Data
    public static  class DeliveryInfoVO{
        /**
         * 发货单编号
         */
        @ApiModelProperty(value = "发货单编号")
        private String code;

        /**
         * 订单商品发货状态(
         * 0:未发货
         * 1:已发货
         * 2:部分发货)
         */
        @ApiModelProperty(value = "订单商品发货状态")
        private DeliveryStatus deliveryStatus;

        /**
         * 发货时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private LocalDateTime deliverTime;


        /**
         * 发货单商品集合
         */
        @ApiModelProperty(value = "发货单商品集合")
        private List<DeliveryItemVO> itemVOList;

        /**
         * 收货人名称
         */
        @ApiModelProperty(value = "收货人名称")
        private String receiverName;

        /**
         * 收货人电话
         */
        @ApiModelProperty(value = "收货人电话")
        private String receiverPhone;

        /**
         * 收获人手机号
         */
        @ApiModelProperty(value = "收获人手机号")
        private String receiverMobile;

        /**
         * 收货地址
         */
        @ApiModelProperty(value = "收货地址")
        private String receiverAddress;

        /**
         * 收货地区
         */
        @ApiModelProperty(value = "收货地区")
        private String areaName;

        /**
         * 快递公司代码
         */
        @ApiModelProperty(value = "快递公司代码")
        private String expressCode;

        /**
         * 快递公司名称
         */
        @ApiModelProperty(value = "快递公司名称")
        private String expressName;

        /**
         * 快递单号
         */
        @ApiModelProperty(value = "快递单号")
        private String expressNo;

        /**
         * 子订单号
         */
        @ApiModelProperty(value = "子订单号")
        private String oid;

        /**
         * 平台单号
         */
        @ApiModelProperty(value = "平台单号")
        private String platformCode;
    }
}
