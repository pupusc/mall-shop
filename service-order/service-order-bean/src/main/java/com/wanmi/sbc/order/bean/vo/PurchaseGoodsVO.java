package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseGoodsVO extends GoodsInfoVO {

    private static final long serialVersionUID = -9202015284827642449L;

    /**
     * spu标题
     */
    @ApiModelProperty(value = "spu标题")
    private String goodsName;

}
