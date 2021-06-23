package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>商品详情模板关联VO</p>
 * @author: sunkun
 * @Date: 2018-10-16
 */
@ApiModel
@Data
public class GoodsTabRelaVO implements Serializable {

    private static final long serialVersionUID = 5847296274783294383L;

    /**
     * spu标识
     */
    @ApiModelProperty(value = "spu标识")
    private String goodsId;

    /**
     * 详情模板id
     */
    @ApiModelProperty(value = "详情模板id")
    private Long tabId;

    /**
     * 内容详情
     */
    @ApiModelProperty(value = "内容详情")
    private String tabDetail;
}
