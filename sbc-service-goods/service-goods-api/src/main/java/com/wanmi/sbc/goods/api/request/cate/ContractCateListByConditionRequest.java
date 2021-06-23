package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据动态条件查询请求结构</p>
 * Created by daiyitian on 2018/11/15.
 */
@ApiModel
@Data
public class ContractCateListByConditionRequest implements Serializable {

    private static final long serialVersionUID = 8663584629048270522L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 商品分类标识
     */
    @ApiModelProperty(value = "商品分类标识")
    private Long cateId;

    /**
     * 商品分类标识列表
     */
    @ApiModelProperty(value = "商品分类标识列表")
    private List<Long> cateIds;
}
