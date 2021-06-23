package com.wanmi.sbc.goods.api.response.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsErpNoResponse implements Serializable {

    private static final long serialVersionUID = 8417773373235879791L;

    @ApiModelProperty(value = "数量")
    private Integer num;
}
