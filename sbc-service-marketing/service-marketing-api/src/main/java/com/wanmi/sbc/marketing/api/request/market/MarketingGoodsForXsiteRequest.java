package com.wanmi.sbc.marketing.api.request.market;
import com.wanmi.sbc.marketing.bean.enums.MarketingLevelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketingGoodsForXsiteRequest {

    /**
     * 商品id列表
     */
    @NotEmpty
    @ApiModelProperty(value = "商品id列表")
    private List<String> goodsInfoIds;

    /**
     * 生效的营销活动
     */
    @ApiModelProperty(value = "生效的营销活动")
    private MarketingLevelType marketingLevelType;
}
