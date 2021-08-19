package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsGoodsLabelUpdateSortRequest implements Serializable {

    @ApiModelProperty(value = "商品标签VO")
    private List<GoodsLabelVO> goodsLabelVOList;

}
