package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据商品SKU编号查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoListByIdRequest implements Serializable {

    private static final long serialVersionUID = -2265501195719873212L;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    @ApiModelProperty(value = "页数")
    private Integer pageNum;

    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoNo;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
}
