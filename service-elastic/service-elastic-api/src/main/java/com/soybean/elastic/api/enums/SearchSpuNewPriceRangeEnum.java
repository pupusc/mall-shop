package com.soybean.elastic.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/26 8:29 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum SearchSpuNewPriceRangeEnum {

    PRICE_RANGE_0_TO_20(101,0, 20, "20元以下", 1),
    PRICE_RANGE_20_TO_40(102,20, 40, "20-40元", 2),
    PRICE_RANGE_40_TO_60(103,40, 60, "40-60元", 3),
    PRICE_RANGE_60_TO_MAX(104, 60, 999999999, "60元及以上", 4);

    private Integer code;

    private Integer from;

    private Integer to;

    private String message;

    private Integer sort;


    public static SearchSpuNewPriceRangeEnum get(Integer code) {
        for (SearchSpuNewPriceRangeEnum spuSortType : values()) {
            if (Objects.equals(spuSortType.getCode(), code)) {
                return spuSortType;
            }
        }
        return null;
    }
}
