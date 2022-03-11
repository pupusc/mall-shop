//package com.wanmi.sbc.goods.api.enums;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.Objects;
//
///**
// * 商品销售平台
// */
//@Getter
//@AllArgsConstructor
//public enum GoodsChannelTypeEnum {
//    MALL_H5("1", "商城-H5"),
//    MALL_MINI("2", "商城-小程序"),
//    MALL_NORMAL("3", "商城-普通分类"),
//    FDDS_DELIVER("4", "樊登读书-实物履约");
//
//    private final String code;
//    private final String desc;
//
//    public static GoodsChannelTypeEnum getByCode(int code) {
//        for (GoodsChannelTypeEnum p : values()) {
//            if (Objects.equals(p.getCode(), code)) {
//                return p;
//            }
//        }
//        return null;
//    }
//}
