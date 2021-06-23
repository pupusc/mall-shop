package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 交易明细结
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = -7517958527707298212L;

    /**
     * 商品图片地址
     */
    private String pic_path;

    /**
     * 单商品原价 单位:元
     */
    private String price;

    /**
     * 税费，单位元
     */
    private String tax_total;

    /**
     * 规格id（无规格商品为0）
     */
    private Long sku_id;

    /**
     * 商品别名
     */
    private String alias;

    /**
     * 子订单号
     */
    private String sub_order_no;

    /**
     * 运杂费，单位元
     */
    private String freight;

    /**
     * 分销税费，单位元
     */
    private String fenxiao_tax_total;

    /**
     * 商品规格编码
     */
    private String outer_sku_id;

    /**
     * 海关编号
     */
    private String customs_code;

    /**
     *
     0 全款预售，1 定金预售
     */
    private String pre_sale_type;

    /**
     * 订单类型，0:普通类型商品; 1:拍卖商品; 5:餐饮商品; 10:分销商品; 20:会员卡商品; 21:礼品卡商品; 23:有赞会议商品; 24:周期购; 30:收银台商品; 31:知识付费商品;
     * 35:酒店商品; 40:普通服务类商品; 71:卡项商品;182:普通虚拟商品; 183:电子卡券商品; 201:外部会员卡商品; 202:外部直接收款商品;
     * 203:外部普通商品; 204:外部服务商品;205:mock不存在商品; 206:小程序二维码;207:积分充值商品;208:付费优惠券商品
     */
    private Long item_type;

    /**
     * 商品数量
     */
    private Long num;

    /**
     * 分销商品单价，单位元
     */
    private String fenxiao_discount_price;

    /**
     * 商品详情链接
     */
    private String goods_url;

    /**
     * 分销单金额 ，单位元
     */
    private String fenxiao_price;

    /**
     * 商品名称
     */
    private String title;

    /**
     * 分销非现金抵扣金额，单位元
     */
    private String fenxiao_discount;

    /**
     * 分销运杂费，单位元
     */
    private String fenxiao_freight;

    /**
     * 是否为预售商品 如果是预售商品则为1
     */
    private String is_pre_sale;

    /**
     * 商品重量 单位 克
     */
    private String weight;

    /**
     * 海淘商品贸易模式
     */
    private String cross_border_trade_mode;

    /**
     * 规格信息（无规格商品为空）
     */
    private String sku_properties_name;

    /**
     * 订单明细id
     */
    private String oid;

    /**
     * 商品最终均摊价 单位:元
     */
    private String payment;

    /**
     * 非现金抵扣金额，单位元
     */
    private String discount;

    /**
     * 是否是跨境海淘订单("1":是,"0":否)
     */
    private String is_cross_border;

    /**
     * 商品积分价（非积分商品则为0）
     */
    private String points_price;

    /**
     * 是否赠品
     */
    private Boolean is_present;

    /**
     * 商品唯一编码
     */
    private String sku_unique_code;

    /**
     * 商品编码
     */
    private String outer_item_id;

    /**
     * 商品id，有赞生成的商品唯一id
     */
    private Long item_id;

    /**
     * 单商品现价，减去了商品的优惠金额 单位:元
     */
    private String discount_price;

    /**
     * 商品留言
     */
    private String buyer_messages;

    /**
     * 分销单实付金额，单位元
     */
    private String fenxiao_payment;

    /**
     * 商品优惠后总价 单位:元
     */
    private String total_fee;

    /**
     * 跨境商品仓库id（仅限大客使用，标准开发者无需关注）
     */
    private String warehouse_code;

    /**
     * 订单商品扩展信息
     */
    private String order_item_extra;

    /**
     * 海淘-申报单价（完税价格）,单位：元
     */
    private String goods_dutiable_price;

    /**
     * 分销海淘-商品总货值（完税价格*商品数量），单位：元
     */
    private String fenxiao_goods_total_dutiable_price;

    /**
     * 海淘-商品总货值（完税价格*商品数量），单位：元
     */
    private String goods_total_dutiable_price;

    /**
     * 海淘-虚拟货币优惠金额（积分、优惠券），单位：元
     */
    private String virtual_currency;

    /**
     * 海淘-促销优惠金额（不含虚拟货币优惠金额），单位：元
     */
    private String promotion_discount;
}
