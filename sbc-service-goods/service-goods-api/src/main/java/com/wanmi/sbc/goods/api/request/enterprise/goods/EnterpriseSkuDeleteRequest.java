package com.wanmi.sbc.goods.api.request.enterprise.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 企业商品删除
 * @author by 柏建忠 on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseSkuDeleteRequest implements Serializable {

    private static final long serialVersionUID = -8312305040718761890L;
    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    @NotEmpty
    private String goodsInfoId;

}
