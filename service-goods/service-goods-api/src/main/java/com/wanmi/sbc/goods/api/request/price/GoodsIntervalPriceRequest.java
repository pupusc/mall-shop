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
 * com.wanmi.sbc.goods.api.request.intervalprice.GoodsIntervalPriceRequest
 * 商品订货区间价格请求对象
 * @author lipeng
 * @dateTime 2018/11/6 下午2:27
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsIntervalPriceRequest implements Serializable {

    private static final long serialVersionUID = -6934160281358797586L;

    @ApiModelProperty(value = "商品信息")
    private List<GoodsInfoDTO> goodsInfoDTOList;
}
