package com.wanmi.sbc.goods.api.response.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.standard.StandardImportGoodsResponse
 * 商品库导入商品请求对象
 * @author lipeng
 * @dateTime 2018/11/9 下午2:48
 */
@ApiModel
@Data
public class StandardImportGoodsResponse implements Serializable {

    private static final long serialVersionUID = -2406224019106354126L;

    @ApiModelProperty(value = "sku Id")
    private List<String> skuIdList;
}
