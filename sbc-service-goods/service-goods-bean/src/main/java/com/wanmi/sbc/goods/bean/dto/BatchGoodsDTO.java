package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品批量导入参数
 * 增加虚拟goodsId，表示与其他商品相关类的数据关联
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchGoodsDTO extends GoodsDTO{

    private static final long serialVersionUID = -8807674093765870168L;

    /**
     * 模拟goodsId
     */
    @ApiModelProperty(value = "模拟goodsId")
    private String mockGoodsId;

}
