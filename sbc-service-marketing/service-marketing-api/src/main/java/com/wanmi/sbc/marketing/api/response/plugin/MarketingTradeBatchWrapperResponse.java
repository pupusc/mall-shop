package com.wanmi.sbc.marketing.api.response.plugin;

import com.wanmi.sbc.marketing.bean.vo.TradeMarketingWrapperVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 批量订单提交营销处理响应结构
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingTradeBatchWrapperResponse implements Serializable {

    private static final long serialVersionUID = 4520038630632475443L;

    /**
     * 批量订单提交营销插件参数 {@link TradeMarketingWrapperVO}
     */
    @ApiModelProperty(value = "批量订单提交营销插件参数")
    private List<TradeMarketingWrapperVO> wraperVOList;
}
