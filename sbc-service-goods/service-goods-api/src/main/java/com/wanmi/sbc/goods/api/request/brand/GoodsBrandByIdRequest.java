package com.wanmi.sbc.goods.api.request.brand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 品牌查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBrandByIdRequest implements Serializable {

    private static final long serialVersionUID = 4390819159191294564L;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    @NotNull
    private Long brandId;
}
