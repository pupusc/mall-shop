package com.wanmi.sbc.marketing.api.response.discount;

import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * Date: 2018-11-20
 * @author Administrator
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingFullDiscountByMarketingIdResponse implements Serializable{

    private static final long serialVersionUID = 1381098439719086104L;

    @ApiModelProperty(value = "营销满折多级优惠列表")
    private List<MarketingFullDiscountLevelVO> marketingFullDiscountLevelVOList;
}
