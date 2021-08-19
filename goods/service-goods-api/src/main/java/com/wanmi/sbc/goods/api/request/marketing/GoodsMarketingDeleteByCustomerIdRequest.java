package com.wanmi.sbc.goods.api.request.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>根据用户编号删除商品使用的营销请求</p>
 * author: sunkun
 * Date: 2018-11-02
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMarketingDeleteByCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = -1892514436553998564L;

    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @NotBlank
    private String customerId;
}
