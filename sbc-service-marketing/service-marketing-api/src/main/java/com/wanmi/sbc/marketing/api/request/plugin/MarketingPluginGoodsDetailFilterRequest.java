package com.wanmi.sbc.marketing.api.request.plugin;

import com.wanmi.sbc.marketing.bean.dto.GoodsInfoDetailByGoodsInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.MarketingPluginDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>商品详情处理请求结构</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketingPluginGoodsDetailFilterRequest extends MarketingPluginDTO {

    private static final long serialVersionUID = -4567058881536968827L;

    @ApiModelProperty(value = "商品详情")
    @NotNull
    private GoodsInfoDetailByGoodsInfoDTO goodsInfoDetailByGoodsInfoDTO;
}
