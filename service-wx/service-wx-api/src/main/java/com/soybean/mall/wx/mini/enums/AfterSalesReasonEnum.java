package com.soybean.mall.wx.mini.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * duanlsh
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AfterSalesReasonEnum {

    AFTERSALES_ONE(1, "拍错/多拍"),
    AFTERSALES_TWO(2, "不想要了"),
    AFTERSALES_THREE(3, "无快递信息"),
    AFTERSALES_FOUR(4, "包裹为空"),
    AFTERSALES_FIVE(5, "已拒签包裹"),
    AFTERSALES_SIX(6, "快递长时间未送达"),
    AFTERSALES_SEVEN(7, "与商品目数不符"),
    AFTERSALES_EIGHT(8, "质量问题"),
    AFTERSALES_NINE(9, "卖家发错货"),
    AFTERSALES_TEN(10, "三无产品"),
    AFTERSALES_ELEVEN(11, "假冒产品"),
    AFTERSALES_OTHER(12, "其他");

    private Integer code;

    private String message;


    public static AfterSalesReasonEnum getByCode(int code) {
        for (AfterSalesReasonEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }

}
