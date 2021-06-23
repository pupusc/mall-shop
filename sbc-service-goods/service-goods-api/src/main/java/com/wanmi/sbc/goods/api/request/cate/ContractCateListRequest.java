package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateListRequest implements Serializable {

    private static final long serialVersionUID = -3186383940108415398L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
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
