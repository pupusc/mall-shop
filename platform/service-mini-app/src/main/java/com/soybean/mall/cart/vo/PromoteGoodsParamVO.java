package com.soybean.mall.cart.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Liang Jun
 * @date 2022-06-20 19:39:00
 */
@Data
public class PromoteGoodsParamVO {
    /**
     * 活动id或优惠券id
     */
    @NotBlank
    private String id;
    /**
     * 搜索关键词
     */
    private String keyword;
    /**
     * 分页页码
     */
    private Integer pageNum = 1;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;
    /**
     * 排序类型 0 默认
     */
    private Integer spuSortType = 0;

    public Integer convertSortType() {
        if (this.spuSortType == null) {
            return 0;
        }
        switch (spuSortType) {
            case 0 : return 0;
            case 1 : return 5;
            case 2 : return 1;
            case 3 : return 6;
            case 4 : return 2;
            case 5 : return 3;
        }
        return spuSortType;
    }
}
