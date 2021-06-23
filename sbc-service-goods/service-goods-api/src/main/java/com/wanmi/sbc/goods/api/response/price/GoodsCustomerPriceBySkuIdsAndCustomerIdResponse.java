package com.wanmi.sbc.goods.api.response.price;

import com.wanmi.sbc.goods.bean.vo.GoodsCustomerPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCustomerPriceBySkuIdsAndCustomerIdResponse implements Serializable {

    private static final long serialVersionUID = -3142031311049998411L;

    @ApiModelProperty(value = "商品客户价格")
    private List<GoodsCustomerPriceVO> goodsCustomerPriceVOList;
}
