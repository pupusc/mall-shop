package com.wanmi.sbc.goods.api.request.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:43 2018/12/13
 * @Description:
 */
@ApiModel
@Data
public class StandardGoodsListUsedGoodsIdRequest implements Serializable {

    private static final long serialVersionUID = -5067720832389913520L;

    @ApiModelProperty(value = "商品库Id")
    private List<String> standardIds;

    @ApiModelProperty(value = "店铺Id")
    private List<Long> storeIds;

}
