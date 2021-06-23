package com.wanmi.sbc.marketing.api.request.marketingsuitssku;

import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsCountBySkuIdRequest  implements Serializable {

    /**
     *  商品编号
     */
    @ApiModelProperty(value ="商品编号")
    private String goodsInfoId;

    /**
     * 营销类型
     */
    @ApiModelProperty(value = "营销类型")
    private MarketingType marketingType;

}
