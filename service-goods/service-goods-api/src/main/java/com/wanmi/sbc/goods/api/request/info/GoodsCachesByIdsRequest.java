package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCachesByIdsRequest implements Serializable {

    private static final long serialVersionUID = 4843437711610900278L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SPU编号")
    @NotEmpty
    private List<String> goodsIds;
}
