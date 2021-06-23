package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsByConditionResponse implements Serializable {

    private static final long serialVersionUID = 6835118386535466752L;

    /**
     * 商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息")
    private List<GoodsVO> goodsVOList;
}
