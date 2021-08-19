package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class GoodsInfoFillEnterpriseRequest {

    @ApiModelProperty("商品列表")
    private List<GoodsInfoVO> goodsInfos;
}
