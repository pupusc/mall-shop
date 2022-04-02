package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description: 1 新品榜 2 畅销排行榜 3 特价书榜
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/20 2:05 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum GoodsBlackListCategoryEnum {

    NEW_BOOKS(1, "新品榜"),
    SELL_WELL_BOOKS(2, "畅销排行榜"),
    SPECIAL_OFFER_BOOKS(3, "特价书榜"),
    UN_SHOW_VIP_PRICE(4, "不享受会员优惠商品"),
    UN_SHOW_WAREHOUSE(5, "查询的虚拟仓库"),
    POINT_NOT_SPLIT(6, "不能使用积分商品");

    private final Integer code;
    private final String message;

    public static GoodsBlackListCategoryEnum getByCode(int code) {
        for (GoodsBlackListCategoryEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
