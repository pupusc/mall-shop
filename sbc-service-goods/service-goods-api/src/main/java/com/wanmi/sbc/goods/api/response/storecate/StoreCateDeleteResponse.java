package com.wanmi.sbc.goods.api.response.storecate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Author: bail
 * Time: 2017/11/13.10:25
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateDeleteResponse implements Serializable {

    private static final long serialVersionUID = 4830809452869579507L;

    @ApiModelProperty(value = "删除店铺分类", notes = "allCate: 所有分类, storeCateGoodsRelas: 店铺商品分类关系")
    private HashMap<String,Object> hashMap;
}
