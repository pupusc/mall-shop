package com.wanmi.sbc.pay.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录请求参数
 */
@Data
public class PayTradeRecordRequest implements Serializable {

    private static final long serialVersionUID = -808773261554727362L;

    /**
     * 业务id(订单或退单号)
     */
    private String businessId;

    /**
     * 支付渠道方返回的支付或退款对象id
     */
    private String chargeId;

    /**
     * 申请价格
     */
    private BigDecimal applyPrice;

    /**
     * 实际成功交易价格
     */
    private BigDecimal practicalPrice;

    /**
     * 支付项(具体支付渠道)id
     */
    private Long channelItemId;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 交易完成时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime finishTime;

    /**
     * 回调时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime callbackTime;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 业务结果
     */
    private String result_code;

    /**
     * appId
     */
    private String appId;

}
