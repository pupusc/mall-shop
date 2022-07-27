package com.soybean.mall.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/25 2:57 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum RecordMessageTypeEnum {


    CREATE_ORDER(1, "创建订单"),
    PAY_ORDER(2, "支付订单"),
    CANCEL_ORDER(3, "取消订单"),;

    private int code;

    private String message;


    public static RecordMessageTypeEnum getByCode(int code) {
        for (RecordMessageTypeEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
