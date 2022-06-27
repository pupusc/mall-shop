package com.soybean.mall.cart.vo;

import lombok.Data;

/**
 * @author Liang Jun
 * @date 2022-06-20 19:39:00
 */
@Data
public class MarketingGoodsParamVO {
    /**
     * 活动id
     */
    private Integer id;
    /**
     * 分页页码
     */
    private Integer pageNum = 1;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;
}
