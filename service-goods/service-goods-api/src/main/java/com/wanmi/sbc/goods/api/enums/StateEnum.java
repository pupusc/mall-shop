package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 1:48 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum StateEnum {

    BEFORE(0, "未开始"),
    RUNNING(1, "进行中"),
    AFTER(2, "已结束");


    private final Integer code;
    private final String message;

    public static StateEnum getByCode(int code) {
        for (StateEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
