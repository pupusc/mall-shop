package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/11 2:18 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class OrderDetailResp {

    /**
     * 订单ID
     */
    private Long tid;

    /**
     * 订单状态
     *
     *
     */
    private Integer status;

    /**
     * 售后单个数
     */
    private Integer saleAfterCount;

//    /**
//     * 销售渠道ID
//     */
//    private Integer saleChannelId;
//
//    /**
//     * 快照销售渠道ID
//     */
//    private Integer fkSaleChannelId;
//
//    /**
//     * 渠道订单号
//     */
//    private String platformOrderId;

    /**
     * 订单号
     */
    private Long orderNumber;

    /**
     * 子订单号
     */
    private String orderItemTid;

    /**
     * 订单金额
     */
    private Integer totalFee;

//    /**
//     * 推广人类型
//     *
//     * @see com.soybean.unified.order.api.enums.UnifiedOrderPromoTypeEnum
//     */
//    private Integer promoType;

    /**
     * 推广人ID
     */
    private Integer promoUserId;

    /**
     * 下单人ID
     */
    private Integer userId;

    /**
     * 下单人手机号
     */
    private String mobile;

    /**
     * 商品总金额，单位：分
     */
    private Integer goodsAmount;

    /**
     * 渠道下单-时间
     */
    private Date bookTime;

    /**
     * 优惠总金额
     */
    private Integer discountFee;

    /**
     * 运费
     */
    private Integer postFee;

    /**
     * 运费成本-粗算 待确定
     */
    private Integer postFeeEstimate;

    /**
     * 运费成本-实际
     */
    private Integer futurePostFee;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付状态：0：未支付，1：已支付，2：部分支付
     */
    private Integer paymentStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 取消/关闭时间
     */
    private Date cancelTime;

    /**
     * 错误原因
     */
    private String errorReason;

    /**
     * 应付金额
     */
    private Integer oughtFee;

    /**
     * 交易完成时间
     */
    private Date completeTime;

    /**
     * 积分支付金额
     */
    private Integer jiFenAmount;

    /**
     * 知豆支付金额
     */
    private Integer zhiDouAmount;

    /**
     * 实付金额
     */
    private Integer actualFee;

    /**
     * 已退金额
     */
    private Integer refundFee;

    /**
     * 买家留言
     */
    private String buyerMemo;

    /**
     * 卖家留言
     */
    private String sellerMemo;

    /**
     * 商户ID
     */
    private String shopId;

    /**
     * 设备类型
     */
    private Integer deviceType;

//    /**
//     * 子订单
//     */
//    private List<OrderItemBO> orderItemBOS;
//
//    /**
//     * 收件信息
//     */
//    private OrderAddressBO orderAddressBO;

    /**
     * 优惠成本承担-渠道平台后返
     */
    private Integer costDiscountFee;
}
