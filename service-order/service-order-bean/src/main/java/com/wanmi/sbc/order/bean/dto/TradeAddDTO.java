package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.bean.enums.OrderSource;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单状态
 * Created by jinwei on 14/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeAddDTO implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * 订单号
     */
    @Id
    @ApiModelProperty(value = "订单号")
    private String id;

    /**
     * 父订单号
     */
    @ApiModelProperty(value = "父订单号")
    private String parentId;

    /**
     * 订单组号
     */
    @ApiModelProperty(value = "订单组号")
    private String groupId;

    /**
     * 购买人
     */
    @ApiModelProperty(value = "购买人")
    private BuyerDTO buyer;

    /**
     * boss卖方
     */
    @ApiModelProperty(value = "boss卖方")
    private SellerDTO seller;

    /**
     * 商家
     */
    @ApiModelProperty(value = "商家")
    private SupplierDTO supplier;

    /**
     * 买家备注
     */
    @ApiModelProperty(value = "买家备注")
    private String buyerRemark;

    /**
     * 卖家备注
     */
    @ApiModelProperty(value = "卖家备注")
    private String sellerRemark;

    /**
     * 订单附件，以逗号隔开
     */
    @ApiModelProperty(value = "订单附件,以逗号隔开")
    private String encloses;

    /**
     * 调用方的请求 IP
     * added by shenchunnan
     */
    @ApiModelProperty(value = "调用方的请求 IP")
    private String requestIp;

    /**
     * 发票
     */
    @ApiModelProperty(value = "发票")
    private InvoiceDTO invoice;

    /**
     * 订单总体状态
     */
    @ApiModelProperty(value = "订单总体状态")
    private TradeStateDTO tradeState;

    /**
     * 收货人信息
     */
    @ApiModelProperty(value = "收货人信息")
    private ConsigneeDTO consignee;

    /**
     * 订单价格
     */
    @ApiModelProperty(value = "订单价格")
    private TradePriceDTO tradePrice;

    /**
     * 订单商品列表
     */
    @ApiModelProperty(value = "订单商品列表")
    private List<TradeItemDTO> tradeItems = new ArrayList<>();

    /**
     * 发货单
     */
    @ApiModelProperty(value = "发货单")
    private List<TradeDeliverDTO> tradeDelivers = new ArrayList<>();

    /**
     * 配送方式
     */
    @ApiModelProperty(value = "配送方式")
    private DeliverWay deliverWay;

    @ApiModelProperty(value = "订单支付信息")
    private PayInfoDTO payInfo;

    /**
     * 支付单ID
     */
    @ApiModelProperty(value = "支付单ID")
    private String payOrderId;

    /**
     * 订单来源方
     */
    @ApiModelProperty(value = "订单来源方")
    private Platform platform;

    /**
     * 下单时是否已开启订单自动审核
     */
    @ApiModelProperty(value = "下单时是否已开启订单自动审核",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean isAuditOpen = true;

    /**
     * 订单支付顺序
     */
    @ApiModelProperty(value = "订单支付顺序")
    private PaymentOrder paymentOrder;

    /**
     * 超时未支付取消订单时间
     */
    @ApiModelProperty(value = "超时未支付取消订单时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;

    /**
     * 操作日志记录（状态变更）
     */
    @ApiModelProperty(value = "操作日志记录", notes = "状态变更")
    private List<TradeEventLogDTO> tradeEventLogs = new ArrayList<>();

    /**
     * 是否被结算
     */
    @ApiModelProperty(value = "是否被结算",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean hasBeanSettled;

    /**
     * 是否可退标识
     */
    @ApiModelProperty(value = "是否可退标识",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean canReturnFlag;

    /**
     * 退款标识
     * 仅供结算使用 - 标识该订单是未收货的退款单子
     * <p>
     * 该单子flowState是作废不会入账，但是退单是COMPLETE状态会入账，导致收支不公，加了单独的状态作为判断
     */
    @ApiModelProperty(value = "退款标识", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean refundFlag;

    /**
     * 订单营销信息
     */
    @ApiModelProperty(value = "订单营销信息")
    private List<TradeMarketingVO> tradeMarketings;

    /**
     * 订单使用的店铺优惠券
     */
    @ApiModelProperty(value = "订单使用的店铺优惠券")
    private TradeCouponVO tradeCoupon;

    /**
     * 营销赠品全量列表
     */
    @ApiModelProperty(value = "营销赠品全量列表")
    private List<TradeItemDTO> gifts = new ArrayList<>();

    /**
     * 订单来源--区分h5,pc,app,小程序,代客下单
     */
    @ApiModelProperty(value = "订单来源")
    private OrderSource orderSource;

    /**
     * 是否拼团订单
     */
    @ApiModelProperty(value = "是否拼团订单")
    private boolean grouponFlag;

    /**
     * 订单拼团信息
     */
    @ApiModelProperty(value = "订单拼团信息")
    private TradeGrouponDTO tradeGroupon;
    /**
     * 虚拟商品直冲手机号
     */
    @ApiModelProperty(value = "虚拟商品直冲手机号")
    private String directChargeMobile;

    /**
     * 外部交易编号
     */
    private String outTradeNo;
    /**
     * 外部交易平台：FDDS:樊登读书
     */
    private String outTradePlat;
}
