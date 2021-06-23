package com.wanmi.sbc.marketing.api.response.reduction;

import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
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
public class MarketingFullReductionByMarketingIdResponse implements Serializable{

    private static final long serialVersionUID = -1610187904946137556L;

    @ApiModelProperty(value = "满减营销级别列表")
    private List<MarketingFullReductionLevelVO> marketingFullReductionLevelVOList;
}
