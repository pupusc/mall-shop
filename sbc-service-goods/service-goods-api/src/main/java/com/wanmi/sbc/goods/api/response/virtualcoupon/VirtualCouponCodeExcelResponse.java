package com.wanmi.sbc.goods.api.response.virtualcoupon;

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
public class VirtualCouponCodeExcelResponse implements Serializable {

    /**
     * base64位字符串形式的文件流
     */
    @ApiModelProperty(value = "base64位字符串形式的文件流")
    private String file;
}
