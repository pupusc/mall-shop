package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description: 业务类型 1、商品skuId 2、商品spuId 3、一级分类id 4、二级分类id
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/20 2:05 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum GoodsBlackListTypeEnum {

    SKU_ID(1, "商品skuId"),
    SPU_ID(2, "商品spuId"),
    CLASSIFY_ID_FIRST(3, "一级分类id"),
    CLASSIFY_ID_SECOND(4, "二级分类id");

    private final Integer code;
    private final String message;

    public static GoodsBlackListTypeEnum getByCode(int code) {
        for (GoodsBlackListTypeEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
