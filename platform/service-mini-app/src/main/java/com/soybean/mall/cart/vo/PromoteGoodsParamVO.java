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
     * 综合：0			h5：0（默认）
     * 书评评分：1		h5：5（评论数）
     * 最新上架：2		h5：1
     * 好评优先：3		h5：6
     * 高价优先：4		h5：2
     * 低价优先：5		h5：3
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
