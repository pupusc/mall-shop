package com.wanmi.sbc.goods.api.request.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 根据商品库id获取商品库Sku查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardSkuByStandardIdRequest implements Serializable {

    private static final long serialVersionUID = -7241428210289350785L;

    @ApiModelProperty(value = "商品库SPUid")
    @NotBlank
    private String standardId;

}
