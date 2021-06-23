package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsModifyAddedStatusRequest
 * 修改商品注水销量请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:51
 */
@ApiModel
@Data
public class GoodsModifyShamSalesNumRequest implements Serializable {

    private static final long serialVersionUID = -6250733406387197138L;

    @NotNull
    @ApiModelProperty(value = "注水销量")
    private Long shamSalesNum;

    @NotBlank
    @ApiModelProperty(value = "商品Id")
    private String goodsId;
}
