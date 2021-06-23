package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 系统配置KEY
 * Created by daiyitian on 2017/4/22.
 */
@ApiEnum(dataType = "java.lang.String")
public enum ConfigType {
    /**
     * yun:云
     */
    @ApiEnumProperty("yun:云")
    YUN("yun"),
    /**
     * kuaidi100
     */
    @ApiEnumProperty("kuaidi100")
    KUAIDI100("kuaidi100"),

    /**
     * 增值税资质审核
     */
    @ApiEnumProperty("增值税资质审核")
    TICKETAUDIT("ticket_aduit"),

    /**
     * 商家审核
     */
    @ApiEnumProperty("商家审核")
    SUPPLIERAUDIT("supplier_audit"),

    /**
     * 商家商品审核
     */
    @ApiEnumProperty("商家商品审核")
    SUPPLIERGOODSAUDIT("supplier_goods_audit"),

    /**
     * 自营商品审核
     */
    @ApiEnumProperty("自营商品审核")
    BOSSGOODSAUDIT("boss_goods_audit"),

    /**
     * 订单审核
     */
    @ApiEnumProperty("订单审核")
    ORDERAUDIT("order_audit"),

    /**
     * 客户审核
     */
    @ApiEnumProperty("客户审核")
    CUSTOMERAUDIT("customer_audit"),

    /**
     * 客户审核
     */
    @ApiEnumProperty("客户审核")
    CUSTOMERINFOAUDIT("customer_info_audit"),

    /**
     *用户设置
     */
    @ApiEnumProperty("用户设置")
    USERAUDIT("user_audit"),

    /**
     * 增值服务-企业购-设置
     */
    @ApiEnumProperty("增值服务-企业购-设置")
    VAS_IEP_SETTING("vas_iep_setting"),

    /**
     * 订单设置自动收货
     */
    @ApiEnumProperty("订单设置自动收货")
    ORDER_SETTING_AUTO_RECEIVE("order_setting_auto_receive"),

    /**
     * 退单自动审核
     */
    @ApiEnumProperty("退单自动审核")
    ORDER_SETTING_REFUND_AUTO_AUDIT("order_setting_refund_auto_audit"),

    /**
     * 退单自动收货
     */
    @ApiEnumProperty("退单自动收货")
    ORDER_SETTING_REFUND_AUTO_RECEIVE("order_setting_refund_auto_receive"),

    /**
     * 允许申请退单
     */
    @ApiEnumProperty("允许申请退单")
    ORDER_SETTING_APPLY_REFUND("order_setting_apply_refund"),

    /**
     * PC商城商品列表默认展示大图或小图
     */
    @ApiEnumProperty("PC商城商品列表默认展示大图或小图")
    PC_GOODS_IMAGE_SWITCH("pc_goods_image_switch"),

    /**
     * PC商城商品列表展示维度SKU或者SPU
     */
    @ApiEnumProperty("PC商城商品列表展示维度SKU或者SPU")
    PC_GOODS_SPEC_SWITCH("pc_goods_spec_switch"),

    /**
     * 移动端商城商品列表默认展示大图或小图
     */
    @ApiEnumProperty("移动端商城商品列表默认展示大图或小图")
    MOBILE_GOODS_IMAGE_SWITCH("mobile_goods_image_switch"),

    /**
     * 移动端商城商品列表展示维度SKU或者SPU
     */
    @ApiEnumProperty("移动端商城商品列表展示维度SKU或者SPU")
    MOBILE_GOODS_SPEC_SWITCH("mobile_goods_spec_switch"),

    /**
     * 订单支付顺序设置（先款后货/不限）
     */
    @ApiEnumProperty("订单支付顺序设置（先款后货/不限）")
    ORDER_SETTING_PAYMENT_ORDER("order_setting_payment_order"),

    /**
     * 超时未支付取消订单
     */
    @ApiEnumProperty("超时未支付取消订单")
    ORDER_SETTING_TIMEOUT_CANCEL("order_setting_timeout_cancel"),

    /**
     * 超时自动评价订单
     */
    @ApiEnumProperty("超时自动评价订单")
    ORDER_SETTING_TIMEOUT_EVALUATE("order_setting_timeout_evaluate"),

    /**
     * 关于我们
     */
    @ApiEnumProperty("关于我们")
    ABOUT_US("about_us"),

    /**
     * app检测升级
     */
    @ApiEnumProperty("app检测升级")
    APP_UPDATE("app_update"),

    /**
     * app分享
     */
    @ApiEnumProperty("app分享")
    APP_SHARE("app_share"),

    /**
     * 小程序分享设置
     */
    @ApiEnumProperty("小程序分享设置")
    APPLET_SHARE_SETTING("applet_share_setting"),

    /**
     * 商品评价设置
     */
    @ApiEnumProperty("商品评价设置")
    GOODS_EVALUATE_SETTING("goods_evaluate_setting"),

    /**
     * 小程序基础配置
     */
    @ApiEnumProperty("小程序基础配置")
    SMALL_PROGRAM_SETTING_CUSTOMER("small_program_setting_customer"),

    /**
     * 成长值基础获取规则类型——签到
     */
    @ApiEnumProperty("成长值基础获取规则类型——签到")
    GROWTH_VALUE_BASIC_RULE_SIGN_IN("growth_value_basic_rule_sign_in"),

    /**
     * 成长值基础获取规则类型——注册
     */
    @ApiEnumProperty("成长值基础规则类型——注册")
    GROWTH_VALUE_BASIC_RULE_REGISTER("growth_value_basic_rule_register"),

    /**
     * 成长值基础规则类型——分享商品
     */
    @ApiEnumProperty("成长值基础规则类型——分享商品")
    GROWTH_VALUE_BASIC_RULE_SHARE_GOODS("growth_value_basic_rule_share_goods"),

    /**
     * 成长值基础规则类型——评论
     */
    @ApiEnumProperty("成长值基础规则类型——评论")
    GROWTH_VALUE_BASIC_RULE_COMMENT_GOODS("growth_value_basic_rule_comment_goods"),

    /**
     * 成长值基础规则类型——完善个人信息
     */
    @ApiEnumProperty("成长值基础规则类型——完善个人信息")
    GROWTH_VALUE_BASIC_RULE_COMPLETE_INFORMATION("growth_value_basic_rule_complete_information"),

    /**
     * 成长值基础获取规则类型——绑定微信
     */
    @ApiEnumProperty("成长值基础规则类型——绑定微信")
    GROWTH_VALUE_BASIC_RULE_BIND_WECHAT("growth_value_basic_rule_bind_wechat"),

    /**
     * 成长值基础获取规则类型——添加收货地址
     */
    @ApiEnumProperty("成长值基础获取规则类型——添加收货地址")
    GROWTH_VALUE_BASIC_RULE_ADD_DELIVERY_ADDRESS("growth_value_basic_rule_add_delivery_address"),

    /**
     * 成长值基础获取规则类型——关注店铺
     */
    @ApiEnumProperty("成长值基础获取规则类型——关注店铺")
    GROWTH_VALUE_BASIC_RULE_FOLLOW_STORE("growth_value_basic_rule_follow_store"),

    /**
     * 成长值基础获取规则类型——分享注册
     */
    @ApiEnumProperty("成长值基础获取规则类型——分享注册")
    GROWTH_VALUE_BASIC_RULE_SHARE_REGISTER("growth_value_basic_rule_share_register"),

    /**
     * 成长值基础获取规则类型——分享购买
     */
    @ApiEnumProperty("成长值基础获取规则类型——分享购买")
    GROWTH_VALUE_BASIC_RULE_SHARE_BUY("growth_value_basic_rule_share_buy"),

    /**
     * 积分基础获取规则类型——签到
     */
    @ApiEnumProperty("积分基础获取规则类型——签到")
    POINTS_BASIC_RULE_SIGN_IN("points_basic_rule_sign_in"),

    /**
     * 积分基础获取规则类型——注册
     */
    @ApiEnumProperty("积分基础获取规则类型——注册")
    POINTS_BASIC_RULE_REGISTER("points_basic_rule_register"),

    /**
     * 积分基础获取规则类型——分享商品
     */
    @ApiEnumProperty("积分基础获取规则类型——分享商品")
    POINTS_BASIC_RULE_SHARE_GOODS("points_basic_rule_share_goods"),

    /**
     * 积分基础获取规则类型——评论
     */
    @ApiEnumProperty("积分基础获取规则类型——评论")
    POINTS_BASIC_RULE_COMMENT_GOODS("points_basic_rule_comment_goods"),

    /**
     * 积分基础获取规则类型——关注店铺
     */
    @ApiEnumProperty("积分基础获取规则类型——关注店铺")
    POINTS_BASIC_RULE_FOLLOW_STORE("points_basic_rule_follow_store"),

    /**
     * 积分基础获取规则类型——完善个人信息
     */
    @ApiEnumProperty("积分基础获取规则类型——完善个人信息")
    POINTS_BASIC_RULE_COMPLETE_INFORMATION("points_basic_rule_complete_information"),

    /**
     * 积分基础获取规则类型——绑定微信
     */
    @ApiEnumProperty("积分基础获取规则类型——绑定微信")
    POINTS_BASIC_RULE_BIND_WECHAT("points_basic_rule_bind_wechat"),

    /**
     * 积分基础获取规则类型——添加收货地址
     */
    @ApiEnumProperty("积分基础获取规则类型——添加收货地址")
    POINTS_BASIC_RULE_ADD_DELIVERY_ADDRESS("points_basic_rule_add_delivery_address"),

    /**
     * 积分基础获取规则类型——分享注册
     */
    @ApiEnumProperty("积分基础获取规则类型——分享注册")
    POINTS_BASIC_RULE_SHARE_REGISTER("points_basic_rule_share_register"),

    /**
     * 积分基础获取规则类型——分享购买
     */
    @ApiEnumProperty("积分基础获取规则类型——分享购买")
    POINTS_BASIC_RULE_SHARE_BUY("points_basic_rule_share_buy"),

    /**
     *小程序直播
     */
    @ApiEnumProperty("小程序直播")
    LIVE("live"),
    /**
     *crm标记
     */
    @ApiEnumProperty("crm标记")
    CRM_FLAG("crm"),

    /**
     * 阿里云客服配置
     */
    @ApiEnumProperty("阿里云客服配置")
    ALIYUN_ONLINE_SERVICE("aliyun_online_service"),

    /**
     * 订单列表展示设置
     */
    @ApiEnumProperty("订单列表展示设置")
    ORDER_LIST_SHOW_TYPE("order_list_show_type"),

    /**
     * 渠道设置-linkedMall
     */
    @ApiEnumProperty("渠道设置-linkedMall")
    THIRD_PLATFORM_LINKED_MALL("third_platform_linked_mall"),

    /**
     * 付费会员设置-付费会员基础信息配置
     */
    @ApiEnumProperty("付费会员设置-付费会员基础信息配置")
    PAID_CARD_CONFIG("paid_card_config");

    private final String value;

    ConfigType(String value) {
        this.value = value;
    }

    @JsonValue
    public String toValue() {
        return value;
    }
}
