package com.wanmi.sbc.order.api.request.trade;


import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.bean.dto.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 提交订单公用方法的参数类型
 * (定义成一个类,是为了后面方便扩展字段)
 * @author bail
 * @date 2018/5/5.13:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeParamsRequest implements Serializable {
    /**
     * 是否后端操作(true:后端代客下单/修改订单 false:前端客户下单)
     */
    @ApiModelProperty(value = "是否后端操作",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean backendFlag;
    /**
     * 是否为下单(true:下单, false:修改订单)
     */
    @ApiModelProperty(value = "是否为下单",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean commitFlag;
    /**
     * 营销活动
     */
    @ApiModelProperty(value = "营销活动")
    private List<TradeMarketingDTO> marketingList;

    /**
     * 选择的店铺优惠券id
     */
    @ApiModelProperty(value = "选择的店铺优惠券id")
    private String couponCodeId;

    /**
     * 订单总价
     */
    @ApiModelProperty(value = "订单总价")
    private TradePriceDTO tradePrice;
    /**
     * 订单商品数据
     */
    @ApiModelProperty(value = "订单商品数据")
    private List<TradeItemDTO> tradeItems;
    /**
     * 旧订单商品数据，用于编辑订单的场景
     */
    @ApiModelProperty(value = "旧订单商品数据，用于编辑订单的场景")
    private List<TradeItemDTO> oldTradeItems;
    /**
     * 旧订单赠品数据，用于编辑订单的场景，由于旧订单赠品库存已先还回但事务未提交，因此在处理中将库存做逻辑叠加
     * 参考的LiuWZ的注释
     */
    @ApiModelProperty(value = "旧订单赠品数据，用于编辑订单的场景，")
    private List<TradeItemDTO> oldGifts;

    /**
     * 客户等级
     */
    @ApiModelProperty(value = "客户等级")
    private CustomerLevelVO storeLevel;
    /**
     * 下单客户
     */
    @ApiModelProperty(value = "下单客户")
    private CustomerVO customer;

    /**
     * 商家
     */
    @ApiModelProperty(value = "商家")
    private SupplierDTO supplier;
    /**
     * 代客下单的操作人(目前不一定是业务员)
     */
    @ApiModelProperty(value = "代客下单的操作人")
    private SellerDTO seller;
    /**
     * 订单来源方
     */
    @ApiModelProperty(value = "订单来源方")
    private Platform platform;

    /**
     * 选择的收货地址id
     */
    @ApiModelProperty(value = "选择的收货地址id")
    private String consigneeId;
    /**
     * 收货地址详细信息(包含省市区)
     */
    @ApiModelProperty(value = "收货地址详细信息")
    private String detailAddress;
    /**
     * 收货地址修改时间
     */
    @ApiModelProperty(value = "收货地址修改时间")
    private String consigneeUpdateTime;
    /**
     * 填写的临时收货地址
     */
    @ApiModelProperty(value = "填写的临时收货地址")
    private ConsigneeDTO consignee;


    /**
     * 发票信息
     */
    @ApiModelProperty(value = "发票信息")
    private InvoiceDTO invoice;
    /**
     * 发票临时收货地址
     */
    @ApiModelProperty(value = "发票临时收货地址")
    private ConsigneeDTO invoiceConsignee;


    /**
     * 配送方式，默认快递
     */
    @ApiModelProperty(value = "配送方式",dataType = "com.wanmi.sbc.goods.bean.enums.DeliverWay")
    private DeliverWay deliverWay;
    /**
     * 支付类型，默认在线支付
     */
   private PayType payType;


    /**
     * 订单买家备注
     */
    @ApiModelProperty(value = "订单买家备注")
    private String buyerRemark;
    /**
     * 订单卖家备注
     */
    @ApiModelProperty(value = "订单卖家备注")
    private String sellerRemark;
    /**
     * 附件, 逗号隔开
     */
    @ApiModelProperty(value = "附件, 逗号隔开")
    private String encloses;


    /**
     * 操作人ip
     */
    @ApiModelProperty(value = "操作人ip")
    private String ip;
    /**
     * 是否强制提交，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    @ApiModelProperty(value = "是否强制提交",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean forceCommit;

    /**
     * 周期购信息
     */
    @ApiModelProperty(value = "周期购信息")
    private CycleBuyInfoDTO cycleBuyInfo;

}
