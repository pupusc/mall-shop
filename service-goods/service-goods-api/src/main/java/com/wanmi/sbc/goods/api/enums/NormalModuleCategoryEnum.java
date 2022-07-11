package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/11 4:27 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@AllArgsConstructor
@Getter
public enum NormalModuleCategoryEnum {

    BACK_POINT(1, "返积分");

    private final Integer code;
    private final String message;

    public static NormalModuleCategoryEnum getByCode(int code) {
        for (NormalModuleCategoryEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
