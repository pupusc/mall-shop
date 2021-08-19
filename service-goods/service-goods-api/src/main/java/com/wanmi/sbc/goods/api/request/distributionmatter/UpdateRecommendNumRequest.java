package com.wanmi.sbc.goods.api.request.distributionmatter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * Created by feitingting on 2019/3/21.
 */
@Data
@ApiModel
public class UpdateRecommendNumRequest {

    @ApiModelProperty(value = "分销素材的ID")
    @NotBlank
    private String id;
}
