package com.wanmi.sbc.account.finance.record.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateTimeDeserializer;
import com.wanmi.ms.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.account.bean.enums.SettleStatus;
import com.wanmi.sbc.common.enums.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结算单
 * Created by hht on 2017/12/6.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "settlement")
@Builder
public class Settlement {

    /**
     * 用于生成结算单号，结算单号自增     FIXME  后期希望使用redis维护自增序列实现
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settle_id")
    private Long settleId;

    /**
     * 结算单uuid，mongodb外键
     */
    @Column(name = "settle_uuid")
    private String settleUuid;

    /**
     * 结算单创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 结算单号
     */
    @Column(name = "settle_code")
    private String settleCode;

    /**
     * 结算单开始时间
     */
    @Column(name = "start_time")
    private String startTime;

    /**
     * 结算单结束时间
     */
    @Column(name = "end_time")
    private String endTime;

    /**
     * 商家Id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 交易总额(已弃用)
     */
    @Column(name = "sale_price")
    private BigDecimal salePrice;

    /**
     * 商品销售总数
     */
    @Column(name = "sale_num")
    private long saleNum;

    /**
     * 退款总额(已弃用)
     */
    @Column(name = "return_price")
    private BigDecimal returnPrice;

    /**
     * 商品退货总数(已弃用)
     */
    @Column(name = "return_num")
    private long returnNum;

    /**
     * 平台佣金总额
     */
    @Column(name = "platform_price")
    private BigDecimal platformPrice;

    /**
     * 店铺应收
     */
    @Column(name = "store_price")
    private BigDecimal storePrice;

    /**
     * 总运费
     */
    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    /**
     * 商品实付总额
     */
    @Column(name = "split_pay_price")
    private BigDecimal splitPayPrice;

    /**
     * 商品供货总额
     */
    @Column(name = "provider_price")
    private BigDecimal providerPrice;

    /**
     * 通用券优惠总额
     */
    @Column(name = "common_coupon_price")
    private BigDecimal commonCouponPrice;

    /**
     * 积分抵扣总额
     */
    @Column(name = "point_price")
    private BigDecimal pointPrice;

    /**
     * 分销佣金总额
     */
    @Column(name = "commission_price")
    private BigDecimal commissionPrice;

    /**
     * 结算时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "settle_time")
    private LocalDateTime settleTime;

    /**
     * 结算状态
     */
    @Column(name = "settle_status")
    private SettleStatus settleStatus;

    /**
     * 商家类型
     */
    @Column(name = "store_type")
    private StoreType storeType;


}
