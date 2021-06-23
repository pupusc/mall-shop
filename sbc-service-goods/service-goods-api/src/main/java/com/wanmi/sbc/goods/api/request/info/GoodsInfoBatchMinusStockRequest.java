package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 商品SKU批量库存减量请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoBatchMinusStockRequest implements Serializable {


    private static final long serialVersionUID = -756726568883495098L;

    /**
     * 批量商品库存数据
     */
    @ApiModelProperty(value = "批量商品库存数据")
    @NotEmpty
    private List<GoodsInfoMinusStockDTO> stockList;



}
