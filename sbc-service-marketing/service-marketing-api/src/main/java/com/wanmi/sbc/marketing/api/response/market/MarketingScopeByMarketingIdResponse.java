package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.marketing.bean.vo.MarketingScopeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingScopeByMarketingIdResponse implements Serializable {

    private static final long serialVersionUID = -5206865899936733495L;

    @ApiModelProperty(value = "营销范围列表")
    private List<MarketingScopeVO> marketingScopeVOList;
}
