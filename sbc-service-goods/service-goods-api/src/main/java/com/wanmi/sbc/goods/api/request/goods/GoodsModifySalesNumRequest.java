package com.wanmi.sbc.goods.api.request.goods;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName GoodsModifySalesNum
 * @Description 更新商品销量Request
 * @Author lvzhenwei
 * @Date 2019/4/11 15:49
 **/
@Data
public class GoodsModifySalesNumRequest implements Serializable {

    private static final long serialVersionUID = 575615072679190734L;

    /**
     * 商品编号
     */
    @NotNull
    private String goodsId;

    /**
     * 商品销量
     */
    @NotNull
    private Long goodsSalesNum;
}
