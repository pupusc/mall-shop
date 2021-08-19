package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunkun on 2017/8/16.
 */
@Data
@ApiModel
public class PurchaseGoodsReponse extends GoodsInfoVO {

    /**
     * spu标题
     */
    @ApiModelProperty(value = "spu标题")
    private String goodsName;
}
