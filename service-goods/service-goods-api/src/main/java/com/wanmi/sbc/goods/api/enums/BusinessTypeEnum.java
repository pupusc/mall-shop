package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 5:20 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum BusinessTypeEnum {
//     1 排行榜 2 书单 3 编辑推荐 4 专题
    RANKING_LIST(1, "排行榜"),
    BOOK_LIST(2, "书单"),
    BOOK_RECOMMEND(3, "编辑推荐"),
    SPECIAL_SUBJECT(4, "专题");


    private final Integer code;
    private final String message;

    public static BusinessTypeEnum getByCode(int code) {
        for (BusinessTypeEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
