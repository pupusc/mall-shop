package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 商品修改请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsInfoModifyRequest extends BaseRequest {

    private static final long serialVersionUID = 6636321075674610130L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    @NotNull
    private GoodsInfoDTO goodsInfo;

}
