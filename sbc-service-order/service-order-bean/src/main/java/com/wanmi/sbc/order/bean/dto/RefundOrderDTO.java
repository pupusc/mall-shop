package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.RefundChannel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundOrderDTO implements Serializable {

    private static final long serialVersionUID = -3674512208386816094L;

    /**
     * 退款单主键
     */
    @ApiModelProperty(value = "退款单主键")
    private String refundId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 退款单编号
     */
    @ApiModelProperty(value = "退款单编号")
    private String returnOrderCode;

    /**
     * 退款单
     */
    @ApiModelProperty(value = "退款单")
    private String refundCode;

    /**
     * 会员详情外键
     */
    @ApiModelProperty(value = "会员详情外键")
    private String customerDetailId;

    /**
     * 退款单状态
     */
    @ApiModelProperty(value = "退款单状态")
    private RefundStatus refundStatus;

    /**
     * del
     */
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    @ApiModelProperty(value = "退款流水")
    private RefundBillDTO refundBill;

    /**
     * 应退金额
     */
    @ApiModelProperty(value = "应退金额")
    private BigDecimal returnPrice;

    /**
     * 应退积分
     */
    @ApiModelProperty(value = "应退积分")
    private Long returnPoints;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private PayType payType;

    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    private String refuseReason;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private Long supplierId;

    /**
     * 退款渠道
     */
    @ApiModelProperty(value = "退款渠道")
    private RefundChannel refundChannel;
}
