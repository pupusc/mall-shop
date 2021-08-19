package com.wanmi.sbc.goods.api.request.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>商品营销</p>
 * author: sunkun
 * Date: 2018-11-02
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMarketingListByCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = 608246770683844223L;

    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @NotBlank
    private String customerId;
}
