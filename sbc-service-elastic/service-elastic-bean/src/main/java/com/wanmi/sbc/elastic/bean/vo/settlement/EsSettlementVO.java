package com.wanmi.sbc.elastic.bean.vo.settlement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结算单
 * Created by hht on 2017/12/6.
 */
@ApiModel
@Data
public class EsSettlementVO implements Serializable {

    private static final long serialVersionUID = 4555211803309442026L;

    /**
     * 用于生成结算单号，结算单号自增
     */
    @ApiModelProperty(value = "结算单id")
    private Long settleId;


    /**
     * 结算单创建时间
     */
    @ApiModelProperty(value = "结算单创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;


    /**
     * 结算单号
     */
    @ApiModelProperty(value = "结算单号")
    private String settleCode;

    /**
     * 结算单号 前端展示修改了定义名称
     */
    @ApiModelProperty(value = "结算单号")
    private String settlementCode;

    /**
     * 结算单开始时间
     */
    @ApiModelProperty(value = "结算单开始时间")
    private String startTime;

    /**
     * 结算单结束时间
     */
    @ApiModelProperty(value = "结算单结束时间")
    private String endTime;

    /**
     * 商家Id
     */
    @ApiModelProperty(value = "商家Id")
    private Long storeId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String storeName;

    /**
     * 交易总额
     */
    @ApiModelProperty(value = "交易总额")
    private BigDecimal salePrice;

    /**
     * 商品销售总数
     */
    @ApiModelProperty(value = "商品销售总数")
    private long saleNum;

    /**
     * 退款总额
     */
    @ApiModelProperty(value = "退款总额")
    private BigDecimal returnPrice;

    /**
     * 商品退货总数
     */
    @ApiModelProperty(value = "商品退货总数")
    private long returnNum;

    /**
     * 平台佣金总额
     */
    @ApiModelProperty(value = "平台佣金总额")
    private BigDecimal platformPrice;

    /**
     * 店铺应收
     */
    @ApiModelProperty(value = "店铺应收")
    private BigDecimal storePrice;

    /**
     * 总运费
     */
    @ApiModelProperty(value = "总运费")
    private BigDecimal deliveryPrice;

    /**
     * 商品实付总额
     */
    @ApiModelProperty(value = "商品实付总额")
    private BigDecimal splitPayPrice;


    /**
     * 供货价总额
     */
    @ApiModelProperty(value = "供货价总额")
    private BigDecimal providerPrice;

    /**
     * 通用券优惠总额
     */
    @ApiModelProperty(value = "通用券优惠总额")
    private BigDecimal commonCouponPrice;

    /**
     * 积分抵扣总额
     */
    @ApiModelProperty(value = "积分抵扣总额")
    private BigDecimal pointPrice;

    /**
     * 分销佣金总额
     */
    @ApiModelProperty(value = "分销佣金总额")
    private BigDecimal commissionPrice;

    /**
     * 结算时间
     */
    @ApiModelProperty(value = "结算单创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime settleTime;

    /**
     * 结算状态
     */
    @ApiModelProperty(value = "结算状态")
    private Integer settleStatus;

}
