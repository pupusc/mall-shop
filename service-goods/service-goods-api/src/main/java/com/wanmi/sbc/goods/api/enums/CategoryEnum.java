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
public enum CategoryEnum {

    BOOK_LIST_MODEL(1, "书单模版"),
    BOOK_CLASSIFY(2, "类目");

    private final Integer code;
    private final String message;

    public static CategoryEnum getByCode(int code) {
        for (CategoryEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
