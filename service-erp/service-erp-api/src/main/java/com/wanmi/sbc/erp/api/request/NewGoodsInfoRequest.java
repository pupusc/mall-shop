package com.wanmi.sbc.erp.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewGoodsInfoRequest implements Serializable {

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    /**
     * 是否有效 1-有效  2-无效
     */
    @ApiModelProperty(value = "商品编码")
    private Integer validFlag;
}
