package com.wanmi.sbc.marketing.api.response.marketingsuitssku;

import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
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
public class MarketinSuitsCountBySkuIdResponse implements Serializable {

    /**
     * 统计数量
     */
    private Long count;

    /**
     * 组合活动关联商品sku表列表结果
     */
    @ApiModelProperty(value = "组合活动关联商品sku表列表结果")
    private List<MarketingSuitsSkuVO> marketingSuitsSkuVOList;

}
