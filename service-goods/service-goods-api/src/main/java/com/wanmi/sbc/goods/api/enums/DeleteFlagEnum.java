package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 8:38 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@AllArgsConstructor
@Getter
public enum DeleteFlagEnum {

    NORMAL(0, "正常"),
    DELETE(1, "删除");

    private final Integer code;
    private final String message;

    public static DeleteFlagEnum getByCode(int code) {
        for (DeleteFlagEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
