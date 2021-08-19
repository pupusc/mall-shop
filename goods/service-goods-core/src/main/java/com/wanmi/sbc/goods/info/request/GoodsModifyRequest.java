package com.wanmi.sbc.goods.info.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GoodsModifyRequest
 * @Description 商品更新request
 * @Author lvzhenwei
 * @Date 2019/4/11 15:42
 **/
@Data
public class GoodsModifyRequest implements Serializable {
    private static final long serialVersionUID = -2076847281822218313L;

    /**
     * 商品编号
     */
    private String goodsId;

    /**
     * 商品销量
     */
    private Long goodsSalesNum;

    /**
     * 商品收藏量
     */
    private Long goodsCollectNum;
}
