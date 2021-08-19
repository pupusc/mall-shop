package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsModifySupplierNameREQUEST
 * 修改商品商家名称
 * @author lipeng
 * @dateTime 2018/11/5 上午10:58
 */
@ApiModel
@Data
public class GoodsModifySupplierNameRequest implements Serializable {

    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    @ApiModelProperty(value = "公司Id")
    private Long companyInfoId;
}
