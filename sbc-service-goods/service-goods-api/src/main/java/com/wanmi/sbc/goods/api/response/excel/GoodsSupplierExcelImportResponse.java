package com.wanmi.sbc.goods.api.response.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goodsexcel.GoodsExcelImportGoodsResponse
 * 商家导入商品响应对象
 * @author lipeng
 * @dateTime 2018/11/2 上午9:55
 */
@ApiModel
@Data
public class GoodsSupplierExcelImportResponse implements Serializable {

    private static final long serialVersionUID = -4449621563625124541L;

    @ApiModelProperty(value = "商家导入商品响应对象")
    private List<String> resultList;
}
