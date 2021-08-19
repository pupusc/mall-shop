package com.wanmi.sbc.distribute.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员中心返回数据
 * Created by CHENLI on 2017/7/17.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopInfoResponse {

    /**
     * 精选店铺名称
     */
    @ApiModelProperty(value = "精选店铺名称")
    private String shopName;

    /**
     * 精选店铺头像
     */
    @ApiModelProperty(value = "精选店铺头像")
    private String headImg;
}
