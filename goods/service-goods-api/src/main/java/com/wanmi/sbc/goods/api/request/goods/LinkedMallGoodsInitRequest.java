package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkedMallGoodsInitRequest implements Serializable {
    @ApiModelProperty("从第几页开始导入")
    @NotNull
    private int pageNum;
}
