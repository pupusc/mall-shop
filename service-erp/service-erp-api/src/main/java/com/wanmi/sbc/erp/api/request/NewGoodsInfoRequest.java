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
    private String metaGoodsCode;
}
