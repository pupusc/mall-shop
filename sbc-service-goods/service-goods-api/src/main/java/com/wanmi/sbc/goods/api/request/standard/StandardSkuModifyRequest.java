package com.wanmi.sbc.goods.api.request.standard;

import com.wanmi.sbc.goods.bean.dto.StandardSkuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品库Sku编辑请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
public class StandardSkuModifyRequest implements Serializable {

    private static final long serialVersionUID = -7241428210289350785L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    @NotNull
    private StandardSkuDTO goodsInfo;

}
