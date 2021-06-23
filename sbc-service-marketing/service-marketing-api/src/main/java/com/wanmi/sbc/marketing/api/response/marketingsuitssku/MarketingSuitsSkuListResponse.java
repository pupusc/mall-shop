package com.wanmi.sbc.marketing.api.response.marketingsuitssku;

import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合活动关联商品sku表列表结果</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsSkuListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 组合活动关联商品sku表列表结果
     */
    @ApiModelProperty(value = "组合活动关联商品sku表列表结果")
    private List<MarketingSuitsSkuVO> marketingSuitsSkuVOList;
}
