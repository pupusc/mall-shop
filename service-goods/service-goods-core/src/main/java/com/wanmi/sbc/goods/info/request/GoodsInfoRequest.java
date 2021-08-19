package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品SKU查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoRequest {

    /**
     * SKU编号
     */
    private String goodsInfoId;

    /**
     * 批量SKU编号
     */
    private List<String> goodsInfoIds;

    /**
     * 客户编号
     */
    private String customerId;

    /**
     * 客户等级
     */
    private Long customerLevelId;

    /**
     * 客户等级折扣
     */
    private BigDecimal customerLevelDiscount;

    /**
     * 是否需要显示规格明细
     * 0:否,1:是
     */
    private Integer isHavSpecText;

    /**
     * 是否需要设置客户商品全局数量
     * 0:否,1:是
     */
    private Integer isHavCusGoodsNum;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 是否删除
     */
    private DeleteFlag deleteFlag;

}
