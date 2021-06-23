package com.wanmi.sbc.goods.api.response.enterprise;

import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApiModel
@Data
public class EnterprisePriceResponse {

    @ApiModelProperty("企业 商品ID--价格")
    private Map<String, BigDecimal> priceMap;

    @ApiModelProperty("企业 商品区间")
    private Map<String, List<GoodsIntervalPriceVO>> intervalMap;

    @ApiModelProperty("商品最小价格")
    private Map<String, BigDecimal> minMap;

    @ApiModelProperty("商品最大价格")
    private Map<String,BigDecimal> maxMap;
}
