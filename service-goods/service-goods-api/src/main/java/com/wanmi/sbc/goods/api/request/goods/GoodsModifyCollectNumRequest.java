package com.wanmi.sbc.goods.api.request.goods;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName GoodsModifyCollectNumRequest
 * @Description 更新商品收藏量Request
 * @Author lvzhenwei
 * @Date 2019/4/11 15:47
 **/
@Data
public class GoodsModifyCollectNumRequest implements Serializable {

    private static final long serialVersionUID = 6783114644003320560L;

    /**
     * 商品编号
     */
    @NotNull
    private String goodsId;

    /**
     * 商品收藏量
     */
    @NotNull
    private Long goodsCollectNum;
}
