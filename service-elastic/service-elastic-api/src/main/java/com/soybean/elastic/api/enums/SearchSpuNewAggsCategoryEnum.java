package com.soybean.elastic.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/26 4:40 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum SearchSpuNewAggsCategoryEnum {

    AGGS_SPU_CATEGORY(100, "商品分类", 100),
    AGGS_PRICE_RANGE(200, "价格区间", 200),
    AGGS_LABEL(300, "服务", 300),
    AGGS_FCLASSIFY(400, "店铺分类", 400),
    AGGS_AUTHOR(500, "作者", 500),
    AGGS_PUBLISHER(600, "出版社", 600),
    AGGS_AWARD(700, "奖项", 700),
    AGGS_CLUMP(800, "丛书", 800),
    AGGS_PRODUCER(900, "出品方", 900),
    ;

    private Integer code;

    private String message;

    private Integer sort;


    public static SearchSpuNewAggsCategoryEnum get(Integer code) {
        for (SearchSpuNewAggsCategoryEnum spuSortType : values()) {
            if (Objects.equals(spuSortType.getCode(), code)) {
                return spuSortType;
            }
        }
        return null;
    }
}
