package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 7:22 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@AllArgsConstructor
@Getter
public enum PublishStateEnum {

    UN_PUBLISH(0, "草稿"),
    EDIT_UN_PUBLISH(1, "已编辑未发布"),
    PUBLISH(1, "已编辑未发布");

    private final int code;
    private final String message;

    public static PublishStateEnum getByCode(int code) {
        for (PublishStateEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
