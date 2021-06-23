package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>神策吗埋点查询一、二、三分类名称</p>
 * author: weiwenhao
 * Date: 2020-06-02
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCateShenceBurialSiteRequest implements Serializable {

    private static final long serialVersionUID = -9057051097784634571L;
    /**
     * 商品分类标识
     */
    @ApiModelProperty(value = "商品分类标识")
    private List<Long> cateIds;

}
