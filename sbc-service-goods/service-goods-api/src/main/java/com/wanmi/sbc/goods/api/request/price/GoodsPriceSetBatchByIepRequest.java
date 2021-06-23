package com.wanmi.sbc.goods.api.request.price;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>企业购商品设价参数</p>
 * Created by of628-wenzhi on 2020-03-04-下午507.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class GoodsPriceSetBatchByIepRequest implements Serializable {
    private static final long serialVersionUID = -8113960455965430995L;
    
    @ApiModelProperty(value = "全量商品SKU信息")
    @NotEmpty
    private List<GoodsInfoVO> goodsInfos;

    @ApiModelProperty(value = "需要企业购商品设价的SKU ID列表，排除了分销商品")
    private List<String> filteredGoodsInfoIds;

    @ApiModelProperty(value = "商品区间价格列表")
    @NotNull
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    @ApiModelProperty(value = "客户信息")
    @NotNull
    private CustomerVO customer;
}
