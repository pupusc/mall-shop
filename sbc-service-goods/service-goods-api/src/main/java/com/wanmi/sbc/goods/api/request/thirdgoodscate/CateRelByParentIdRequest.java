package com.wanmi.sbc.goods.api.request.thirdgoodscate;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel
public class CateRelByParentIdRequest implements Serializable {
    @ApiModelProperty("渠道来源")
    private ThirdPlatformType source;
    @ApiModelProperty("分类父id")
    private Long cateParentId;
}
