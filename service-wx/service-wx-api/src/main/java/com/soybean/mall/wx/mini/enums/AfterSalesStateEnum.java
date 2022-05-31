package com.soybean.mall.wx.mini.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Description: 命名后续要更改
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/21 2:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AfterSalesStateEnum {

    AFTER_SALES_STATE_ONE(1, "用户取消"),
    AFTER_SALES_STATE_TWO(2, "商家受理退款申请中"),
    AFTER_SALES_STATE_THREE(3, "商家拒绝退款"),
    AFTER_SALES_STATE_FOUR(4, "商家拒绝退货退款"),
    AFTER_SALES_STATE_FIVE(5, "商家拒绝退货"),
    AFTER_SALES_STATE_FIX(6, "待用户退货"),
    AFTER_SALES_STATE_SEVEN(7, "售后单关闭"),
    AFTER_SALES_STATE_EIGHT(8, "待商家收货"),
    AFTER_SALES_STATE_ELEVEN(11, "平台退款中"),
    AFTER_SALES_STATE_THIRTEEN(13, "退款成功"),
    AFTER_SALES_STATE_TWENTY_ONE(21, "平台处理退款申请中"),
    AFTER_SALES_STATE_TWENTY_THREE(23, "商家处理退货申请中"),
    AFTER_SALES_STATE_TWENTY_FOUR(24, "平台处理退货申请中"),
    AFTER_SALES_STATE_TWENTY_FIVE(25, "平台处理退货申请中"),


    ;

    private Integer code;

    private String message;


    public static AfterSalesStateEnum getByCode(int code) {
        for (AfterSalesStateEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
