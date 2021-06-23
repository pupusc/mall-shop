package com.wanmi.sbc.account.finance.record.model.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>对账明细返回数据结构</p>
 * Created by of628-wenzhi on 2017-12-08-下午5:06.
 */
@Data
public class AccountDetails implements Serializable {

    private static final long serialVersionUID = -13053197663316687L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 支付方式
     */
    private PayWay payWay;

    /**
     * 金额,单位元，格式："￥#,###.00"
     */
    private String amount;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 下单/退单时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTime;

    /**
     * 支付/退款时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tradeTime;

    /**
     * 优惠金额，单位元，格式："￥#,###.00"
     */
    private BigDecimal discounts;

    /**
     * 交易流水号
     */
    private String tradeNo;
}
