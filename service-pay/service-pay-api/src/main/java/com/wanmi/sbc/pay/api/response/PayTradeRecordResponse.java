package com.wanmi.sbc.pay.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.bean.enums.TradeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>支付交易记录返回结构</p>
 * Created by of628-wenzhi on 2018-08-09-下午4:46.
 */
@ApiModel
@Data
public class PayTradeRecordResponse implements Serializable{

    private static final long serialVersionUID = 5742265513858256132L;

    @ApiModelProperty(value = "支付交易记录id")
    private String id;

    /**
     * 业务id(订单或退单号)
     */
    @ApiModelProperty(value = "业务id(订单或退单号)")
    private String businessId;

    /**
     * 支付渠道方返回的支付或退款对象id
     */
    @ApiModelProperty(value = "支付渠道方返回的支付或退款对象id")
    private String chargeId;

    /**
     * 申请价格
     */
    @ApiModelProperty(value = "申请价格")
    private BigDecimal applyPrice;

    /**
     * 实际成功交易价格
     */
    @ApiModelProperty(value = "实际成功交易价格")
    private BigDecimal practicalPrice;

    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型")
    private TradeType tradeType;

    /**
     * 状态:0处理中(退款状态)/未支付(支付状态) 1成功 2失败
     */
    @ApiModelProperty(value = "状态")
    private TradeStatus status;

    /**
     * 支付项(具体支付渠道)id
     */
    @ApiModelProperty(value = "支付项(具体支付渠道)id")
    private Long channelItemId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 交易完成时间
     */
    @ApiModelProperty(value = "交易完成时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime finishTime;

    /**
     * 回调时间
     */
    @ApiModelProperty(value = "回调时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime callbackTime;

    /**
     * 客户端ip
     */
    @ApiModelProperty(value = "客户端ip")
    private String clientIp;

    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;

    private String appId;
}
