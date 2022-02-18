package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/2/18 6:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class TradeItemSimpleVO implements Serializable {

    private String oid;

    private String spuId;

    private String spuName;

    private String skuId;

    private String skuName;

    private String skuNo;

    private String cateName;

    private String pic;

    /**
     * 商品购买数量
     */
    private Long buyNum;

    /**
     * 商品已经发货数量(用于判断退款)
     */
    private Long deliveredNum = 0L;

    /**
     *  真实发货数量
     */
    private Long deliveredNumHis = 0L;

    /**
     * 商品发货数量
     */
    private DeliverStatus deliverStatus;

    /**
     * 成交价
     */
    private String price;

    /**
     * 商品属性的定价
     */
    private Double propPrice;

    /**
     * 商品定金
     */
    private BigDecimal earnestPrice;

    /**
     * 定金膨胀
     */
    private BigDecimal swellPrice;

    /**
     * 尾款
     */
    private BigDecimal tailPrice;

    /**
     * 定金支付开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelStartTime;

    /**
     * 定金支付结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelEndTime;

    /**
     * 尾款支付开始时间
     */
    @ApiModelProperty(value = "尾款支付开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailStartTime;

    /**
     * 尾款支付结束时间
     */
    @ApiModelProperty(value = "尾款支付结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailEndTime;


    /**
     * 商品原价 - 建议零售价
     */
    private BigDecimal originalPrice;

    /**
     * 商品价格 - 会员价 & 阶梯设价
     */
    private BigDecimal levelPrice;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 平摊小计 - 最初由 levelPrice*num（购买数量） 计算
     */
    private BigDecimal splitPrice;

    /**
     * 可退数量
     */
    private Integer canReturnNum;

    /**
     * 积分 数值
     */
    private Long points;

    /**
     * 积分兑换金额
     */
    private BigDecimal pointsPrice;

    /**
     * 知豆，被用于知豆订单的商品知豆，普通订单的均摊知豆
     */
    private Long knowledge;

    /**
     * 积分兑换金额
     */
    private BigDecimal knowledgePrice;

    /**
     * 结算价格
     */
    private BigDecimal settlementPrice;

    /**
     * 是否是秒杀抢购商品
     */
    private Boolean isFlashSaleGoods=Boolean.FALSE;

    /**
     * 秒杀抢购商品Id
     */
    private Long flashSaleGoodsId;
    /**
     * 是否是加价购换购商品
     */
    private Boolean isMarkupGoods;
    /**
     * 是否是预约抢购商品
     */
    private Boolean isAppointmentSaleGoods = Boolean.FALSE;

    /**
     * 抢购活动Id
     */
    private Long appointmentSaleId;


    /**
     * 是否是预售商品
     */
    private Boolean isBookingSaleGoods = Boolean.FALSE;

    /**
     * 预售活动Id
     */
    private Long bookingSaleId;

    /**
     * 预售类型
     */
    private BookingType bookingType;

    /**
     * 供应商id
     */
    private Long providerId;

    /**
     * 供应商名称
     */
    private String providerName;

    /**
     * 商品状态
     */
    private GoodsStatus goodsStatus;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    private Integer cpsSpecial;

    /**
     * 期数
     */
    private Integer cycleNum;

    /**
     * ERP商品SKU编码
     */
    private String erpSkuNo;

    /**
     * ERP商品SPU编码
     */
    private String erpSpuNo;


    /**
     * 是否是组合商品，0：否，1：是
     */
    private Boolean combinedCommodity;

    /**
     * 退款完成数量
     */
    private Integer returnCompleteNum;

    /**
     * 退款完成金额
     */
    private String returnCompletePrice;

    /**
     * 退款中数量
     */
    private Integer returnIngNum;

    /**
     * 退款中金额
     */
    private String returnIngPrice;
}
