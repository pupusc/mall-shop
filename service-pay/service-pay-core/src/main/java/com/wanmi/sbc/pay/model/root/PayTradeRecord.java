package com.wanmi.sbc.pay.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.bean.enums.TradeType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录
 * Created by sunkun on 2017/8/2.
 */
@Data
@Entity
@Table(name = "pay_trade_record")
public class PayTradeRecord {

    @Id
    @Column(name = "id")
    private String id;

    /**
     * 业务id(订单或退单号)
     */
    @Column(name = "business_id")
    private String businessId;

    /**
     * 支付渠道方返回的支付或退款对象id
     */
    @Column(name = "charge_id")
    private String chargeId;

    /**
     * 申请价格
     */
    @Column(name = "apply_price")
    private BigDecimal applyPrice;

    /**
     * 实际成功交易价格
     */
    @Column(name = "practical_price")
    private BigDecimal practicalPrice;

    /**
     * 交易类型
     */
    @Column(name = "trade_type")
    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    /**
     * 状态:0处理中(退款状态)/未支付(支付状态) 1成功 2失败
     */
    @Column(name = "status")
    @Enumerated
    private TradeStatus status;

    /**
     * 支付项(具体支付渠道)id
     */
    @Column(name = "channel_item_id")
    private Long channelItemId;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 交易完成时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    /**
     * 回调时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "callback_time")
    private LocalDateTime callbackTime;

    /**
     * 客户端ip
     */
    @Column(name = "client_ip")
    private String clientIp;

    /**
     * 交易流水号
     */
    @Column(name = "trade_no")
    private String tradeNo;

    @Column(name = "app_id")
    private String appId;
}
