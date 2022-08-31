package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/17 4:09 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum AnchorPushEnum {

    FAN_DENG_JIANG_SHU("1", "樊登讲书", 1),
    FEI_FAN_JING_DU("2", "非凡精读", 2),
    FAN_DENG_ZHI_BO("3", "樊登直播", 3),
    LI_LEI_MAN_DU("4", "李蕾讲经典", 4);

    private final String code;
    private final String message;
    private final Integer order;

    public static AnchorPushEnum getByCode(String code) {
        for (AnchorPushEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
