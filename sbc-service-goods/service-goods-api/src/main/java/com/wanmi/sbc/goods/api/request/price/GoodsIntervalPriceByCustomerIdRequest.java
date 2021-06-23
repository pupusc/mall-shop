package com.wanmi.sbc.goods.api.request.price;

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
 * com.wanmi.sbc.goods.api.request.intervalprice.GoodsIntervalPriceByCustomerIdRequest
 *
 * @author lipeng
 * @dateTime 2018/11/13 上午9:46
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsIntervalPriceByCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = -4389009176473155060L;

    @ApiModelProperty(value = "商品信息")
    private List<GoodsInfoDTO> goodsInfoDTOList;

    @ApiModelProperty(value = "用户Id")
    private String customerId;
}
