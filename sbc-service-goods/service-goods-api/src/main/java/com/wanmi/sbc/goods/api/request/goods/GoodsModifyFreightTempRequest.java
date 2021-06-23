package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsModifyFreightTempRequest
 * 修改运费模板请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午11:01
 */
@ApiModel
@Data
public class GoodsModifyFreightTempRequest implements Serializable {

    private static final long serialVersionUID = 6874334643738715676L;

    @ApiModelProperty(value = "运费模板Id")
    private Long freightTempId;

    @ApiModelProperty(value = "商品Id")
    private List<String> goodsIds;

}
