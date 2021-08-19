package com.wanmi.sbc.linkedmall.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GoodsDetailQueryRequest implements Serializable {
    @ApiModelProperty("商品id")
    @Size(min = 1)
    private List<Long> itemIds;
}
