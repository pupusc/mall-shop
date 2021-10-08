package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 1:51 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum FilterRuleEnum {

    OUT_OF_STOCK_SHOW(1, "无库存展示"),
    OUT_OF_STOCK_UN_SHOW(2, "无库存不展示"),
    OUT_OF_STOCK_BOTTOM(3, "无库存沉底");

    private final Integer code;
    private final String message;

    public static FilterRuleEnum getByCode(int code) {
        for (FilterRuleEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
