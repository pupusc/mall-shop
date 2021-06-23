package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 商品sku填充商品状态请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoFillGoodsStatusRequest implements Serializable {

    private static final long serialVersionUID = 1155286274397367391L;

    /**
     * 商品SKU信息列表
     */
    @ApiModelProperty(value = "商品SKU信息列表")
    private List<GoodsInfoDTO> goodsInfos;
}
