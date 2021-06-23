package com.wanmi.sbc.goods.api.request.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>根据用户编号和商品编号列表删除商品使用的营销请求</p>
 * author: sunkun
 * Date: 2018-11-02
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest implements Serializable {

    private static final long serialVersionUID = 8976853170615340770L;

    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @NotBlank
    private String customerId;

    /**
     * sku编号
     */
    @ApiModelProperty(value = "sku编号")
    @NotNull
    @Size(min = 1)
    private List<String> goodsInfoIds;
}
