//package com.sbc.wanmi.erp.bean.enums;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonValue;
//import com.wanmi.sbc.common.annotation.ApiEnum;
//import com.wanmi.sbc.common.annotation.ApiEnumProperty;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @program: sbc-background
// * @description: 订单推送状态枚举
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-30 13:55
// **/
//@ApiEnum
//public enum ERPTradePayChannel {
//
//    @ApiEnumProperty("zhifubao: 支付宝")
//    aliPay("zhifubao", "支付宝"),
//
//    @ApiEnumProperty("weixin:微信支付")
//    weixin("weixin", "微信支付"),
//
//    @ApiEnumProperty("wangyin:网银在线")
//    wangyin("wangyin", "网银在线"),
//
//    @ApiEnumProperty("QTZF:其他支付")
//    other("QTZF","其他支付"),
//
//    @ApiEnumProperty("COUPONPAY:优惠券/码全额抵扣")
//    COUPONPAY("COUPONPAY", "优惠券/码全额抵扣"),
//
//    @ApiEnumProperty("JFZF:积分抵扣")
//    JFZF("JFZF", "积分抵扣"),
//
//    @ApiEnumProperty("point:积分支付")
//    point("point", "积分支付");
//
//    ERPTradePayChannel(String stateId, String description) {
//        this.stateId = stateId;
//        this.description = description;
//    }
//
//    private String stateId;
//
//    private String description;
//
//    private static Map<String, ERPTradePushStatus> tradePushERPStatusMap = new HashMap<>();
//
//    static {
//        Arrays.asList(ERPTradePushStatus.values())
//                .forEach(
//                        t -> tradePushERPStatusMap.put(t.getStateId(), t)
//                );
//    }
//
//    @JsonCreator
//    public static ERPTradePushStatus forValue(String stateId){
//        return tradePushERPStatusMap.get(stateId);
//    }
//
//    @JsonValue
//    public String toValue() {
//        return this.getStateId();
//    }
//
//    public String getStateId() {
//        return stateId;
//    }
//
//    public void setStateId(String stateId) {
//        this.stateId = stateId;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//}
