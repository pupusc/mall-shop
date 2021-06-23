package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单扩展信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderExtra implements Serializable {

    private static final long serialVersionUID = 3596965185477480383L;

    /**
     * 下单设备号
     */
    private String create_device_id;

    /**
     * 是否子单(分销买家订单) 是：1 其他：null
     */
    private String is_sub_order;

    /**
     * 分销单外部支付流水号
     */
    private String fx_outer_transaction_no;

    /**
     * 收银员id
     */
    private String cashier_id;

    /**
     * 发票抬头
     */
    private String invoice_title;

    /**
     * 海淘身份证信息
     */
    private String id_card_number;

    /**
     * 是否会员订单
     */
    private String is_member;

    /**
     * 分销单订单号
     */
    private String fx_order_no;

    /**
     * 使用了同一张优惠券&优惠码的多笔订单对应的虚拟总单号
     */
    private String promotion_combine_id;

    /**
     * 美业分店id
     */
    private String dept_id;

    /**
     * 身份证姓名信息
     */
    private String id_card_name;

    /**
     * 团购返现优惠金额
     */
    private Long tm_cash;

    /**
     * 是否是积分订单：1：是 0：不是
     */
    private String is_points_order;

    /**
     * 收银员名字
     */
    private String cashier_name;

    /**
     * 分销单内部支付流水号
     */
    private String fx_inner_transaction_no;

    /**
     * 父单号
     */
    private String parent_order_no;

    /**
     * 结算时间
     */
    private String settle_time;

    /**
     * 是否来自购物车 是：true 不是：false
     */
    private String is_from_cart;

    /**
     * 采购单订单号
     */
    private String purchase_order_no;

    /**
     * 下单人昵称
     */
    private String buyer_name;

    /**
     * 支付营销优惠
     */
    private String pay_ump_detail;

    /**
     * 推广方式
     */
    private String marketing_channel;

    /**
     * 是否父单(分销合并订单) 是：1 其他：null
     */
    private String is_parent_order;

    /**
     * 虚拟总单号：一次下单发生拆单时，会生成一个虚拟总单号
     */
    private String orders_combine_id;

    /**
     * 订单返现金额
     */
    private Long cash;

    /**
     * 拆单时店铺维度的虚拟总单号：发生拆单时，单个店铺生成了多笔订单会生成一个店铺维度的虚拟总单号
     */
    private String kdt_dimension_combine_id;

    /**
     * 团购返现最大返现金额
     */
    private Long t_cash;

    /**
     * 分销店铺id
     */
    private String fx_kdt_id;

    /**
     * 正面身份证图片
     */
    private String id_card_front_photo;

    /**
     * 反面身份证图片
     */
    private String id_card_back_photo;

    /**
     * 推广渠道；快手: KUAI_SHOU, 虎牙: H_Y, 陌陌: MO_MO, 喜马拉雅： XIMA, 国通星驿星: GTXYX, 酷狗: KUGOU, 映客: YING_KE, LesPark: LES_PARK
     */
    private String marketing_order_source;

    /**
     * 导购信息
     */
    private String daogou;

    /**
     * 订单附加费用信息
     */
    private String extra_prices;

}
