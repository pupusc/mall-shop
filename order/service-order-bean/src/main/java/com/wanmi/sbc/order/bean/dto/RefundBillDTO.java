package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款流水
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundBillDTO implements Serializable {

    private static final long serialVersionUID = 8624243240129360993L;

    /**
     * 退款流水主键
     */
    @ApiModelProperty(value = "退款流水主键")
    private String refundBillId;

    /**
     * 退款单外键
     */
    @ApiModelProperty(value = "退款单外键")
    private String refundId;

    /**
     * 退款流水编号
     */
    @ApiModelProperty(value = "退款流水编号")
    private String refundBillCode;

    /**
     * 线下平台账户
     */
    @ApiModelProperty(value = "线下平台账户")
    private Long offlineAccountId;

    /**
     * 线上平台账户
     */
    @ApiModelProperty(value = "线上平台账户")
    private String onlineAccountId;

    /**
     * 客户收款账号id
     */
    @ApiModelProperty(value = "客户收款账号id")
    private String customerAccountId;

    /**
     * 退款评论
     */
    @ApiModelProperty(value = "退款评论")
    private String refundComment;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标识")
    private DeleteFlag delFlag;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    @ApiModelProperty(value = "退款单")
    @JsonBackReference
    private RefundOrderDTO refundOrder;

    /**
     * 实退金额
     */
    @ApiModelProperty(value = "实退金额")
    private BigDecimal actualReturnPrice;

    /**
     * 实退积分
     */
    @ApiModelProperty(value = "实退积分")
    private Long actualReturnPoints;

    /**
     * 退款在线渠道
     */
    @ApiModelProperty(value = "退款在线渠道")
    private String payChannel;

    @ApiModelProperty(value = "支付渠道Id")
    private Long payChannelId;

}
