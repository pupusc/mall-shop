package com.soybean.mall.wx.mini.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/5 5:05 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AfterSalesTypeEnum {
    RETURN(2, "退货退款"),
    REFUND(1, "仅退款不退货");


    private Integer code;

    private String message;


    public static AfterSalesTypeEnum getByCode(int code) {
        for (AfterSalesTypeEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
