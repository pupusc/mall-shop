package com.wanmi.sbc.order.open.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Liang Jun
 * @date 2022-04-02 14:36:00
 */
@Data
public class UserOrderCreateParam {
    /**
     * 合作方订单号
     */
    @NotBlank
    private String tradeNo;
    /**
     * 	账号类型 1：手机号 2：樊登openid接入 3：第三方账号接入
     */
    @NotNull
    private Integer accountType = 1;

    /**
     * 购买人手机号，当accountType为1时必填，注意：
     * 1.如果该手机号已经注册樊登读书账号，则会直充到该账户上；
     * 2.如果该手机号未注册樊登读书账号，则我方系统后台会默认给该手机号注册一个新的樊登读书账号，并发送注册成功短信通知；
     */
    @NotBlank
    private String mobile;

    /**
     * 手机区号，默认+86
     */
    private String areaCode;

//    /**
//     * 分销商品类型 1：樊登讲书会期 2：书籍包 3：单本 4：定制会员 5：课程 6：非凡单本 7：非凡专辑 8:非凡会期 9:训练营 10 跨sku打包 11 课程合集 12 李蕾慢读会期
//     */
//    @NotNull
//    private Integer productType;
//
//    /**
//     * 商品编号
//     */
//    @NotBlank
//    private String productNo;

    /**
     * 合作方支付方式：99-其他（无支付渠道费），1-IAP为苹果支付（支付渠道费30%），2-支付宝 3-微信（支付渠道费0.6%）
     */
    @NotBlank
    private String payType;

    /**
     * 支付日期
     */
    private LocalDateTime payTime;

    /**
     * 外部产品编码
     */
    @NotBlank
    private String externalProductNo;
}
