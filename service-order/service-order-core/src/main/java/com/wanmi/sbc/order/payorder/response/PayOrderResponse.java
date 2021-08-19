package com.wanmi.sbc.order.payorder.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单返回对象
 * Created by zhangjin on 2017/4/27.
 */
@Data
public class PayOrderResponse implements Serializable{

    /**
     * 支付单Id
     */
    private String payOrderId;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 会员id
     */
    private String customerId;


    /**
     * 支付类型
     */
    private PayType payType;

    /**
     * 付款状态
     */
    private PayOrderStatus payOrderStatus;

    /**
     * 备注
     */
    private String comment;

    /**
     * 收款单账号
     * 收款账户前端展示 农业银行6329***7791 支付宝支付189**@163.com
     */
    private String receivableAccount;

    /**
     * 直接展示收款单账号
     * 收款账户前端正常直接展示
     */
    private String normalReceivableAccount;

    /**
     * 流水号
     */
    private String receivableNo;

    /**
     * 收款金额
     */
    private BigDecimal payOrderPrice;

    /**
     * 支付单积分
     */
    private Long payOrderPoints;


    /**
     * 支付单知豆
     */
    private Long payOrderKnowledge;

    /**
     * 应付金额
     */
    private BigDecimal totalPrice;

    /**
     * 收款时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime receiveTime;

    /**
     * 收款在线渠道
     */
    private String payChannel;

    private Long payChannelId;

    /**
     * 商家编号
     */
    private Long companyInfoId;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 附件
     */
    private String encloses;

    /**
     * 支付单对应的订单状态
     */
    private TradeState tradeState;

}
