//package com.wanmi.sbc.order.open.model;
//
//import lombok.Data;
//import org.apache.commons.lang3.StringUtils;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Objects;
//
///**
// * @author Liang Jun
// * @date 2022-04-02 14:36:00
// */
//@Data
//public class FddsOrderCreateParam {
//    /**
//     * 合作方订单号
//     */
//    @NotBlank
//    private String tradeNo;
//    /**
//     * 	账号类型 1：手机号 2：樊登openid接入 3：第三方账号接入
//     */
//    @NotNull
//    private Integer accountType = 1;
//
//    /**
//     * 购买人手机号，当accountType为1时必填，注意：
//     * 1.如果该手机号已经注册樊登读书账号，则会直充到该账户上；
//     * 2.如果该手机号未注册樊登读书账号，则我方系统后台会默认给该手机号注册一个新的樊登读书账号，并发送注册成功短信通知；
//     */
//    @NotBlank
//    private String mobile;
//
//    /**
//     * 手机区号，默认+86
//     */
//    private String areaCode;
//
////    /**
////     * 分销商品类型 1：樊登讲书会期 2：书籍包 3：单本 4：定制会员 5：课程 6：非凡单本 7：非凡专辑 8:非凡会期 9:训练营 10 跨sku打包 11 课程合集 12 李蕾慢读会期
////     */
////    @NotNull
////    private Integer productType;
////
////    /**
////     * 商品编号
////     */
////    @NotBlank
////    private String productNo;
//
//    /**
//     * 平台优惠价（单位分）
//     */
//    @NotNull
//    private BigDecimal plDiscontPrice;
//
//    /**
//     * 合作方支付方式：99-其他（无支付渠道费），1-IAP为苹果支付（支付渠道费30%），2-支付宝 3-微信（支付渠道费0.6%）
//     */
//    @NotBlank
//    private String payType;
//
//    /**
//     * 支付日期
//     */
//    private LocalDateTime payTime;
//
//    /**
//     * 外部产品编码
//     */
//    @NotBlank
//    private String externalProductNo;
//
//
//    /**
//     * 推广人编号类型
//     * 1:站外推广人编号，2:openid，3:thrid，4:樊登用户编号 5:手机号
//     */
//    private String promoterType;
//
//    /**
//     * 推广人编号
//     */
//    private String promoterNo;
//
//    /**
//     * 验证下单参数，失败返回错误信息
//     */
//    public String checkParams() {
//        if (StringUtils.isBlank(tradeNo)) {
//            return "合作方订单号不能为空";
//        }
//        if (Objects.isNull(accountType)) {
//            return "账号类型不能为空";
//        }
//        if (StringUtils.isBlank(mobile)) {
//            return "购买人手机号不能为空";
//        }
//        if (Objects.isNull(plDiscontPrice)) {
//            return "平台优惠价不能为空";
//        }
//        if (StringUtils.isBlank(payType)) {
//            return "合作方支付方式不能为空";
//        }
//        if (StringUtils.isBlank(externalProductNo)) {
//            return "外部产品编码不能为空";
//        }
//        return null;
//    }
//}
