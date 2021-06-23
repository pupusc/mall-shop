package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkedmallAddRequest {

    @ApiModelProperty("linkedmall-itemid")
    @NotNull
    private List<Long> itemIds;

    @ApiModelProperty("linkedmall类目id")
    private List<Long> LinkedMallCateIds;
}
