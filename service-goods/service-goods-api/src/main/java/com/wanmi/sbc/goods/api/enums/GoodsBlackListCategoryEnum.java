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
    POINT_NOT_SPLIT(6, "不能使用积分商品"),
    CLASSIFT_AT_BOTTOM(7, "底部分类"),
    GOODS_SESRCH_AT_INDEX(8, "首页商品搜索H5和领阅不展示"),
    GOODS_SESRCH_H5_AT_INDEX(9, "首页商品搜索H5不展示"),
    UN_USE_GOODS_COUPON(10, "下单不可以使用优惠券"),
    UN_BACK_POINT_AFTER_PAY(11, "下单支付后返回积分客户黑名单");

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
