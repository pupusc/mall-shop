package com.wanmi.sbc.customer.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description: 变更来源
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/14 3:22 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum FanDengChangeTypeEnum {

    plus(1, "增加"),
    minus(2, "减少");

    private final Integer code;
    private final String message;

    public static FanDengChangeTypeEnum getByCode(int code) {
        for (FanDengChangeTypeEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
