package com.wanmi.sbc.returnorder.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退货商品数量修改
 * Created by jinwei on 25/4/2017.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnItemNum {

    @ApiModelProperty(value = "skuId")
    private String skuId;

    @ApiModelProperty(value = "数量")
    private Integer num;
}
